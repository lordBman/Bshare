import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Home from './home';
import Splash from './splash';
import Info from './info';
import { RootStackParamList } from '../utils/types';

const Stack = createNativeStackNavigator<RootStackParamList>();

const Screens = () =>{
    return (
        <Stack.Navigator initialRouteName="Splash" screenOptions={{ headerTintColor: '#e91e63' }}>
            <Stack.Screen name="Home" component={Home} options={{ title: 'B-Share', headerShown: true, headerBackVisible: false }} />
            <Stack.Screen name="Splash" component={Splash} options={{ headerShown: false}} />
            <Stack.Screen name="Info" component={Info} options={{ title: 'About', headerShown: true, headerBackVisible: true}} />
        </Stack.Navigator>
    );
}

export default Screens;