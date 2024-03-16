import {baseURL} from "../util/BookingUtil";
import axios from "axios";
import {AvailableTime, Domain} from "../interface/BookingTypes";

export const getAvailableTimes = (from: any, until: any, workshopName: any, vehicleType: any) => {
    return axios.get(baseURL + '/available-times?from=' + from + '&until=' + until
        + "&workshopNames=" + workshopName + "&vehicleTypes=" + vehicleType);
}

export const bookTime = (availableTime: AvailableTime) => {
    return axios.post(baseURL + '/book-time', availableTime);
}