import {baseURL} from "../util/BookingUtil";
import axios from "axios";
import {AvailableTime} from "../interface/BookingTypes";

export const getAvailableTimes = (from: any, until: any, workshopName: any, vehicleType: any) => {
    return axios.get(baseURL + '/available-times?from=' + from + '&until=' + until
        + "&workshopNames=" + workshopName + "&vehicleTypes=" + vehicleType);
}

export const bookTime = (selectedTime: AvailableTime) => {
    return axios.post(baseURL + '/book-time', selectedTime);
}