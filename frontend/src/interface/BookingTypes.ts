export interface AvailableTime {
    availableTimeId: string,
    time: string,
    workshop: Workshop[]
}

export interface Workshop {
    address: string,
    name: string,
    vehicleType: VehicleType[]
}

export interface VehicleType {
    type: string;
}