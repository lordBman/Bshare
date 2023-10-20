import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from "react-native-vector-icons/MaterialIcons";

import Files from "./files";
import Settings from "./settings";
import Connect from "./connect";

const Tab = createBottomTabNavigator();

const Home = () =>{
    return (
        <Tab.Navigator initialRouteName="connect" screenOptions={{ tabBarActiveTintColor: '#e91e63', headerShown: false }}>
            <Tab.Screen name="connect" component={Connect}
                options={{ tabBarLabel: 'Connect', tabBarIcon: ({ color, size }) => (<Icon name="wifi" color={color} size={size} />),}} />
            <Tab.Screen name="files" component={Files}
                options={{ tabBarLabel: 'Files', tabBarIcon: ({ color, size }) => (<Icon name="folder" color={color} size={size} />),}} />
            <Tab.Screen name="settings" component={Settings}
                options={{ tabBarLabel: 'Settings', tabBarIcon: ({ color, size }) => (<Icon name="settings" color={color} size={size} />),}} />
        </Tab.Navigator>
    );
}

export default Home;