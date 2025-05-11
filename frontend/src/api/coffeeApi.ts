// api/coffeeApi.ts
import axios from 'axios';

const API_URL = 'https://coffeeshoptemp.onrender.com/api/coffees';

export interface CoffeeDto {
    id: number;
    name: string;
    type: string;
    price: number;
}

export interface CoffeeWithOrdersDto extends CoffeeDto {
    orders: Array<{
        id: number;
        user: {
            id: number;
            name: string;
            phoneNumber: string;
        };
        notes: string;
    }>;
}

export const getCoffees = async (): Promise<CoffeeDto[]> => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getCoffeeWithOrders = async (id: number): Promise<CoffeeWithOrdersDto> => {
    const response = await axios.get(`${API_URL}/${id}/orders`);
    return response.data;
};

export const createCoffee = async (data: Omit<CoffeeDto, 'id'>): Promise<CoffeeDto> => {
    const response = await axios.post(API_URL, data);
    return response.data;
};

export const updateCoffee = async (id: number, data: Partial<CoffeeDto>): Promise<CoffeeDto> => {
    const response = await axios.patch(`${API_URL}/${id}`, data);
    return response.data;
};

export const deleteCoffee = async (id: number): Promise<void> => {
    await axios.delete(`${API_URL}/${id}`);
};