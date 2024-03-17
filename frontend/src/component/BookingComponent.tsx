import {
    Alert,
    Box,
    Button,
    Container,
    CssBaseline,
    FormControl,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
    SelectChangeEvent,
    Snackbar,
    Theme,
    Typography,
    useTheme
} from "@mui/material";

import dayjs, {Dayjs} from 'dayjs';
import {DemoContainer} from '@mui/x-date-pickers/internals/demo';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import {DatePicker} from "@mui/x-date-pickers";
import React, {FormEvent, useEffect, useState} from "react";
import {bookTime, getAvailableTimes, getWorkshops} from "../service/BookingService";
import {AvailableTime, Workshop} from "../interface/BookingTypes";

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

function getStyles(name: string, personName: string[], theme: Theme) {
    return {
        fontWeight:
            personName.indexOf(name) === -1
                ? theme.typography.fontWeightRegular
                : theme.typography.fontWeightMedium,
    };
}

export default function BookingComponent() {
    const theme = useTheme();

    const [from, setFrom] = useState<Dayjs | null>(dayjs('2024-01-01'));
    const [until, setUntil] = useState<Dayjs | null>(dayjs('2024-01-31'));
    const [workshopName, setWorkshopName] = useState<string[]>([]);
    const [vehicleType, setVehicleType] = useState<string[]>([]);

    const [isWorkshopNameEmpty, setIsWorkshopNameEmpty] = useState<boolean>(false);
    const [isVehicleTypeEmpty, setIsVehicleTypeEmpty] = useState<boolean>(false);

    const [availableTimes, setAvailableTimes] = useState<AvailableTime[]>([]);
    const [selectedTime, setSelectedTime] = useState<AvailableTime>();

    const [successMessage, setSuccessMessage] = useState<boolean>(false);
    const [selectedTimeMissing, setSelectedTimeMissing] = useState<boolean>(false);
    const [noAvailableTimesFound, setNoAvailableTimesFound] = useState<boolean>(false);

    const [workshops, setWorkshops] = useState<Workshop[]>([]);

    const [selectorState, setSelectorState] = useState(-1);

    useEffect(() => {
        getWorkshops().then((response) => {
            setWorkshops(response.data)
        });
    }, []);

    const getWorkshopsAvailableTimes = () => {
        getAvailableTimes(from?.format('YYYY-MM-DD'), until?.format('YYYY-MM-DD'), workshopName, vehicleType)
            .then((response) => {
                setAvailableTimes(response.data.availableTimes)
            })
            .catch((error) => {
                setNoAvailableTimesFound(true);
            })
    }

    function DisplayAvailableTimes() {
        return (
            <>{
                availableTimes?.map((availableTime, index) =>
                    <div key={availableTime.availableTimeId}>
                        <Box
                            height={50}
                            width={500}
                            my={4}
                            display="flex"
                            alignItems="center"
                            gap={2}
                            p={2}
                            sx={{ border: '2px solid grey', backgroundColor: selectorState === index ? 'lightgreen' : '2px solid grey' }}
                            onClick={() => handleSelect(availableTime, index)}
                        >
                            {availableTime.time}
                        </Box>
                    </div>
                )
            }</>
        )
    }

    const handleSearch = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        validateSearchForm(data);
    };

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        validateSelectForm(data);
    };

    const handleSelect = (availableTime: AvailableTime, index: any) => {
        setSelectorState(index);
        setSelectedTime(availableTime);
    };

    const validateSearchForm = (data: FormData) => {
        setIsWorkshopNameEmpty(false);
        setIsVehicleTypeEmpty(false);

        if (!data.get('workshops')) {
            setIsWorkshopNameEmpty(true);
        }
        if (!data.get('vehicleTypes')) {
            setIsVehicleTypeEmpty(true);
        }
        if (data.get('workshops') && data.get('vehicleTypes')) {
            getWorkshopsAvailableTimes();
        }
    }

    const validateSelectForm = (data: FormData) => {
        setSelectedTimeMissing( false);

        if (!selectedTime) {
            setSelectedTimeMissing(true);
        }
        if (selectedTime) {
            submitForm(selectedTime);
        }
    }

    const submitForm = (selectedTime: any) => {
        const availableTime: AvailableTime = {
            availableTimeId: selectedTime.availableTimeId,
            time: selectedTime.time,
            workshop: selectedTime.workshop,
            // TODO: add field contact information to form
            contactInformation: "test"
        }

        bookTime(availableTime).then((response) => {
            setSuccessMessage(true);
            getWorkshopsAvailableTimes();
            setSelectorState(-1);
        });
    }

    const handleClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }

        setNoAvailableTimesFound(false);
        setSuccessMessage(false);
        setSelectedTimeMissing(false);
    };

    const handleWorkshopNameChange = (event: SelectChangeEvent<typeof workshopName>) => {
        const {
            target: { value },
        } = event;
        setWorkshopName(
            typeof value === 'string' ? value.split(',') : value,
        );
    }

    const handleVehicleTypeChange = (event: SelectChangeEvent<typeof vehicleType>) => {
        const {
            target: { value },
        } = event;
        setVehicleType(
            typeof value === 'string' ? value.split(',') : value,
        );
    }

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <Box component="form" noValidate onSubmit={handleSearch} sx={{ mt: 3 }}>
                    <Typography component="h1" variant="h5">
                        Welcome to tire change booking system.
                    </Typography>

                    <Typography component="h1">
                        Please select date for your booking and workshop you wish to visit.
                    </Typography>

                    <FormControl sx={{m: 1, width: 400}}>
                        <InputLabel
                            id="workshops"> Workshop
                        </InputLabel>
                        <Select
                            labelId="workshops"
                            id="workshops"
                            name="workshops"
                            multiple
                            value={workshopName}
                            required={true}
                            error={isWorkshopNameEmpty}
                            onChange={handleWorkshopNameChange}
                            input={<OutlinedInput label="Name"/>}
                            MenuProps={MenuProps}
                        >
                            {workshops.map((workshop) => (
                                <MenuItem
                                    key={workshop.name}
                                    value={workshop.name}
                                    style={getStyles(workshop.name, workshopName, theme)}
                                >
                                    {workshop.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControl sx={{m: 1, width: 400}}>
                        <InputLabel
                            id="vehicleTypes"> Vehicle type
                        </InputLabel>
                        <Select
                            labelId="vehicleTypes"
                            id="vehicleTypes"
                            name="vehicleTypes"
                            multiple
                            value={vehicleType}
                            error={isVehicleTypeEmpty}
                            onChange={handleVehicleTypeChange}
                            input={<OutlinedInput label="Name2"/>}
                            MenuProps={MenuProps}
                        >
                            {workshops.map((workshop) => (
                                workshop.vehicleType.map((vehicleType) =>
                                    <MenuItem
                                        key={vehicleType.type}
                                        value={vehicleType.type}
                                        style={getStyles(vehicleType.type, workshopName, theme)}
                                    >
                                        {vehicleType.type}
                                    </MenuItem>
                                )
                            ))}
                        </Select>
                    </FormControl>

                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DemoContainer components={['DatePicker', 'DatePicker']}>
                            <DatePicker
                                name="from"
                                label="From"
                                value={from}
                                onChange={(newValue) => setFrom(newValue)}
                            />
                            <DatePicker
                                name="until"
                                label="Until"
                                value={until}
                                onChange={(newValue) => setUntil(newValue)}
                            />
                        </DemoContainer>
                    </LocalizationProvider>

                    <Button
                        type="submit"
                        variant="contained"
                        sx={{mt: 3, mb: 2}}
                    >
                        Search
                    </Button>

                    <DisplayAvailableTimes></DisplayAvailableTimes>
                </Box>

                <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
                    <div>
                        {
                            availableTimes.length > 0 ?
                                <Button
                                    type="submit"
                                    variant="contained"
                                    sx={{mt: 3, mb: 2}}
                                >
                                    Book time
                                </Button> : <div></div>
                        }
                    </div>

                    <Snackbar open={successMessage} autoHideDuration={6000} onClose={handleClose}>
                        <Alert
                            onClose={handleClose}
                            severity="success"
                            variant="filled"
                            sx={{ width: '100%' }}
                        >
                            Selected time has been booked.
                        </Alert>
                    </Snackbar>

                    <Snackbar open={selectedTimeMissing} autoHideDuration={6000} onClose={handleClose}>
                        <Alert
                            onClose={handleClose}
                            severity="error"
                            variant="filled"
                            sx={{ width: '100%' }}
                        >
                            Please select time to book.
                        </Alert>
                    </Snackbar>

                    <Snackbar open={noAvailableTimesFound} autoHideDuration={6000} onClose={handleClose}>
                        <Alert
                            onClose={handleClose}
                            severity="error"
                            variant="filled"
                            sx={{ width: '100%' }}
                        >
                            No available times in selected date range.
                        </Alert>
                    </Snackbar>
                </Box>
            </Box>
        </Container>
    )
}