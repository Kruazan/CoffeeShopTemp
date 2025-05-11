import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Modal, Form, Input, InputNumber, Collapse } from 'antd';
import type { CoffeeDto, CoffeeWithOrdersDto } from '../types';
import { getCoffees, createCoffee, updateCoffee, deleteCoffee, getCoffeeWithOrders } from '../api/coffeeApi';

const { Panel } = Collapse;

interface CoffeeTabProps {
    showMessage: (options: {
        type: 'success' | 'error' | 'info';
        content: string;
        duration?: number;
        onClose?: () => void;
    }) => void;
}


const CoffeeTab: React.FC<CoffeeTabProps> = ({ showMessage }) => {
    const [coffees, setCoffees] = useState<CoffeeDto[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [currentCoffee, setCurrentCoffee] = useState<CoffeeDto | null>(null);
    const [form] = Form.useForm();
    const [expandedCoffee, setExpandedCoffee] = useState<CoffeeWithOrdersDto | null>(null);

    useEffect(() => {
        fetchCoffees();
    }, []);


    const fetchCoffees = async () => {
        setLoading(true);
        try {
            const data = await getCoffees();
            setCoffees(data);
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при загрузке кофе' });
        } finally {
            setLoading(false);
        }
    };

    const fetchCoffeeDetails = async (id: number) => {
        try {
            const data = await getCoffeeWithOrders(id);
            setExpandedCoffee(data);
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при загрузке деталей' });
        }
    };

    const handleAdd = () => {
        setCurrentCoffee(null);
        form.resetFields();
        setIsModalVisible(true);
    };

    const handleEdit = (record: CoffeeDto) => {
        setCurrentCoffee(record);
        form.setFieldsValue(record);
        setIsModalVisible(true);
    };

    const handleDelete = async (id: number) => {
        try {
            await deleteCoffee(id);
            showMessage({ type: 'success', content: 'Кофе удалено' });
            fetchCoffees();
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при удалении' });
        }
    };

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            if (currentCoffee) {
                await updateCoffee(currentCoffee.id, values);
                showMessage({ type: 'success', content: 'Кофе обновлено' });
            } else {
                await createCoffee(values);
                showMessage({ type: 'success', content: 'Кофе добавлено' });
            }
            setIsModalVisible(false);
            fetchCoffees();
        } catch (error) {
            showMessage({ type: 'error', content: 'Ошибка при сохранении' });
        }
    };

    const columns = [
        {
            title: 'Название',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Тип',
            dataIndex: 'type',
            key: 'type',
        },
        {
            title: 'Цена',
            dataIndex: 'price',
            key: 'price',
            render: (price: number) => `${price} ₽`,
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_: any, record: CoffeeDto) => (
                <Space size="middle">
                    <Button
                        onClick={() => handleEdit(record)}
                        style={{ backgroundColor: '#8B5A2B', color: 'white' }}
                    >
                        Редактировать
                    </Button>
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

            <div style={{
                backgroundColor: 'white',
                padding: '20px',
                borderRadius: '10px',
                boxShadow: '0 4px 8px rgba(0,0,0,0.1)'
            }}>
                <Button
                    type="primary"
                    onClick={handleAdd}
                    style={{
                        marginBottom: 16,
                        backgroundColor: '#6F4E37',
                        borderColor: '#6F4E37'
                    }}
                >
                    Добавить кофе
                </Button>

                <Table
                    columns={columns}
                    dataSource={coffees}
                    rowKey="id"
                    loading={loading}
                    style={{
                        backgroundColor: 'white',
                        borderRadius: '8px'
                    }}
                    expandable={{
                        expandedRowRender: (record) => (
                            <Collapse accordion>
                                <Panel
                                    header="Заказы с этим кофе"
                                    key="orders"
                                    style={{
                                        backgroundColor: '#F5F5DC'
                                    }}
                                >
                                    {expandedCoffee?.orders?.length ? (
                                        <ul style={{ listStyle: 'none', padding: 0 }}>
                                            {expandedCoffee.orders.map((order) => (
                                                <li
                                                    key={order.id}
                                                    style={{
                                                        padding: '10px',
                                                        marginBottom: '8px',
                                                        backgroundColor: '#FFF8E1',
                                                        borderRadius: '5px'
                                                    }}
                                                >
                                                    <strong>Заказ #{order.id}</strong> - {order.user.name} ({order.user.phoneNumber})
                                                    <br />
                                                    <span style={{ color: '#5D4037' }}>Примечание: {order.notes || 'нет'}</span>
                                                </li>
                                            ))}
                                        </ul>
                                    ) : (
                                        <p style={{ color: '#5D4037' }}>Это кофе еще не заказывали</p>
                                    )}
                                </Panel>
                            </Collapse>
                        ),
                        onExpand: (expanded, record) => {
                            if (expanded) {
                                fetchCoffeeDetails(record.id);
                            }
                        },
                    }}
                />
            </div>

            <Modal
                title={currentCoffee ? 'Редактировать кофе' : 'Добавить кофе'}
                open={isModalVisible}
                onOk={handleSubmit}
                onCancel={() => setIsModalVisible(false)}
                okButtonProps={{
                    style: {
                        backgroundColor: '#6F4E37',
                        borderColor: '#6F4E37'
                    }
                }}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="name"
                        label="Название"
                        rules={[{ required: true, message: 'Введите название кофе' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        name="type"
                        label="Тип"
                        rules={[{ required: true, message: 'Введите тип кофе' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        name="price"
                        label="Цена"
                        rules={[
                            { required: true, message: 'Введите цену' },
                            { type: 'number', min: 0, message: 'Цена должна быть положительной' },
                        ]}
                    >
                        <InputNumber style={{ width: '100%' }} step={0.01} />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default CoffeeTab;