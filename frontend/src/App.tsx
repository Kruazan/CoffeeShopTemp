import React, { useState } from 'react';
import { message, Layout, Tabs } from 'antd';
import CoffeeTab from './components/CoffeeTab';
import OrdersTab from './components/OrdersTab';

const { Content } = Layout;
const { TabPane } = Tabs;

// Определите тип для параметров сообщения
type MessageOptions = {
    type: 'success' | 'error' | 'info';
    content: string;
    duration?: number;
    onClose?: () => void;
};

const App = () => {
    const [activeTab, setActiveTab] = useState('coffee');

    // Функция теперь принимает объект options
    const showMessage = (options: MessageOptions) => {
        const { type, content, duration, onClose } = options;
        message[type](content, duration, onClose);
    };

    return (
        <Layout style={{ minHeight: '100vh', padding: '24px' }}>
            <Content>
                <Tabs activeKey={activeTab} onChange={setActiveTab}>
                    <TabPane tab="Кофе" key="coffee">
                        <CoffeeTab showMessage={showMessage} />
                    </TabPane>
                    <TabPane tab="Заказы" key="orders">
                        <OrdersTab showMessage={showMessage} />
                    </TabPane>
                </Tabs>
            </Content>
        </Layout>
    );
};

export default App;