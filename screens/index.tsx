import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Home from './home';
import Splash from './splash';
import Info from './info';
import { RootStackParamList } from '../utils/types';

const Stack = createNativeStackNavigator<RootStackParamList>();

const Screens = () =>{
    return (
        <Stack.Navigator initialRouteName="home" screenOptions={{ headerTintColor: '#e91e63' }}>
            <Stack.Screen name="home" component={Home} options={{ title: 'B-Share', headerShown: true, headerBackVisible: false }} />
            <Stack.Screen name="splash" component={Splash} options={{ headerShown: false}} />
            <Stack.Screen name="info" component={Info} options={{ title: 'About', headerShown: true, headerBackVisible: true}} />
        </Stack.Navigator>
    );
}

export default Screens;