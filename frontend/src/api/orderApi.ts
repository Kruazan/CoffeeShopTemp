// api/orderApi.ts
import axios from 'axios';

const API_URL = 'https://coffeeshoptemp.onrender.com/api/orders';

export interface DisplayOrderDto {
    id: number;
    user: {
        id: number;
        name: string;
        phoneNumber: string;
    };
    coffees: Array<{
        id: number;
        name: string;
        type: string;
        price: number;
    }>;
    notes: string;
}

export interface CreateOrderDto {
    userId: number;
    coffeeIds: number[];
    notes?: string;
}

export const getOrders = async (): Promise<DisplayOrderDto[]> => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const createOrder = async (data: CreateOrderDto): Promise<DisplayOrderDto> => {
    const response = await axios.post(API_URL, data);
    return response.data;
};

export const updateOrder = async (id: number, data: CreateOrderDto): Promise<DisplayOrderDto> => {
    const response = await axios.patch(`${API_URL}/${id}`, data);
    return response.data;
};

export const deleteOrder = async (id: number): Promise<void> => {
    await axios.delete(`${API_URL}/${id}`);
};