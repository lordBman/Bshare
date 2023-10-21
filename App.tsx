import React from 'react';
import { SafeAreaView, StatusBar, View, useColorScheme } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import Screens from './screens';
import LinkingConfig from './utils/linking';

const App = () =>{
    return (
        <NavigationContainer>
            <Screens />
        </NavigationContainer>
    );
}

export default App;