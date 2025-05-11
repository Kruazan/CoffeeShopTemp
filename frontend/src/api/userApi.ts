// api/userApi.ts
import axios from 'axios';

const API_URL = 'http://localhost:8080/users';

export interface UserDto {
    id: number;
    name: string;
    phoneNumber: string;
}

export interface DisplayUserDto {
    id: number;
    phoneNumber: string;
    name: string;
}


export const getUsers = async (): Promise<UserDto[]> => {
    const response = await axios.get(API_URL);
    return response.data;
};