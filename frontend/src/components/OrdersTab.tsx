import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select } from 'antd';
import type { DisplayOrderDto, CreateOrderDto, CoffeeDto, DisplayUserDto } from '../types';
import { getOrders, createOrder, deleteOrder } from '../api/orderApi';
import { getCoffees } from '../api/coffeeApi';
import { getUsers } from '../api/userApi';

const { Option } = Select;
const { TextArea } = Input;

interface OrdersTabProps {
    showMessage: (options: {
        type: 'success' | 'error' | 'info';
        content: string;
        duration?: number;
        onClose?: () => void;
    }) => void;
}

const OrdersTab: React.FC<OrdersTabProps> = ({ showMessage }) => {
    const [orders, setOrders] = useState<DisplayOrderDto[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [form] = Form.useForm();
    const [coffees, setCoffees] = useState<CoffeeDto[]>([]);
    const [users, setUsers] = useState<DisplayUserDto[]>([]);

    useEffect(() => {
        fetchOrders();
        fetchCoffees();
        fetchUsers();
    }, []);

    const fetchOrders = async () => {
        setLoading(true);
        try {
            const data = await getOrders();
            setOrders(data);
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при загрузке заказов' });
        } finally {
            setLoading(false);
        }
    };

    const fetchCoffees = async () => {
        try {
            const data = await getCoffees();
            setCoffees(data);
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при загрузке кофе' });
        }
    };

    const fetchUsers = async () => {
        try {
            const data = await getUsers();
            setUsers(data);
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при загрузке пользователей' });
        }
    };

    const handleAdd = () => {
        form.resetFields();
        setIsModalVisible(true);
    };

    const handleDelete = async (id: number) => {
        try {
            await deleteOrder(id);
            showMessage({ type: 'success', content: 'Заказ удален' });
            fetchOrders();
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при удалении' });
        }
    };

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            const orderData: CreateOrderDto = {
                userId: values.userId,
                coffeeIds: values.coffeeIds,
                notes: values.notes,
            };

            await createOrder(orderData);
            showMessage({ type: 'success', content: 'Заказ создан' });
            setIsModalVisible(false);
            fetchOrders();
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при сохранении' });
        }
    };

    const columns = [
        {
            title: 'Пользователь',
            key: 'user',
            render: (_: any, record: DisplayOrderDto) => (
                <span>
                    {record.user.name} ({record.user.phoneNumber})
                </span>
            ),
        },
        {
            title: 'Кофе',
            key: 'coffees',
            render: (_: any, record: DisplayOrderDto) => (
                <span>
                    {record.coffees.map(c => c.name).join(', ')}
                </span>
            ),
        },
        {
            title: 'Примечание',
            dataIndex: 'notes',
            key: 'notes',
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_: any, record: DisplayOrderDto) => (
                <Space size="middle">
                    <Button
                        danger
                        onClick={() => handleDelete(record.id)}
                        style={{ backgroundColor: '#A52A2A', color: 'white' }}
                    >
                        Удалить
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <div style={{
            backgroundColor: '#F5F5DC',
            minHeight: '100vh',
            padding: '20px'
        }}>
            <h1 style={{
                color: '#5D4037',
                textAlign: 'center',
                fontSize: '36px',
                marginBottom: '30px',
                fontFamily: 'cursive',
                textShadow: '2px 2px 4px rgba(0,0,0,0.1)'
            }}>
                CoffeeShop
            </h1>

            <Button
                type="primary"
                onClick={handleAdd}
                style={{
                    backgroundColor: '#8B5A2B',
                    borderColor: '#8B5A2B',
                    color: 'white',
                    marginBottom: 16
                }}
            >
                Создать заказ
            </Button>

            <Table
                columns={columns}
                dataSource={orders}
                rowKey="id"
                loading={loading}
                bordered
                style={{ backgroundColor: 'white' }}
            />

            <Modal
                title="Создать заказ"
                open={isModalVisible}
                onOk={handleSubmit}
                onCancel={() => setIsModalVisible(false)}
                width={600}
                okButtonProps={{
                    style: {
                        backgroundColor: '#8B5A2B',
                        borderColor: '#8B5A2B',
                        color: 'white'
                    }
                }}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="userId"
                        label="Пользователь"
                        rules={[{ required: true, message: 'Выберите пользователя' }]}
                    >
                        <Select placeholder="Выберите пользователя">
                            {users.map(user => (
                                <Option key={user.id} value={user.id}>
                                    {user.name} ({user.phoneNumber})
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="coffeeIds"
                        label="Кофе"
                        rules={[{ required: true, message: 'Выберите хотя бы одно кофе' }]}
                    >
                        <Select mode="multiple" placeholder="Выберите кофе">
                            {coffees.map(coffee => (
                                <Option key={coffee.id} value={coffee.id}>
                                    {coffee.name} ({coffee.type}) - {coffee.price}₽
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item name="notes" label="Примечание">
                        <TextArea rows={4} />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default OrdersTab;
