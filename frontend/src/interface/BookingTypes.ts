export interface AvailableTime {
    availableTimeId: string,
    time: string,
    workshop: Workshop[],
    contactInformation: string;
}

export interface Workshop {
    address: string,
    name: string,
    vehicleType: VehicleType[]
}

export interface VehicleType {
    type: string;
}

export interface Domain {
    availableTimes: AvailableTime[],
    availableTime: AvailableTime
}