import React from 'react';
import { SafeAreaView, StatusBar, View, useColorScheme } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import Screens from './screens';
import LinkingConfig from './utils/linking';

const App = () =>{
    const colorScheme = useColorScheme();

    const isDarkMode = colorScheme === 'dark';

    const backgroundStyle = {
        backgroundColor: isDarkMode ? "#0f0f0f" : "white",
    };

    return (
        <NavigationContainer>
            <Screens headerColor = {backgroundStyle.backgroundColor} />
        </NavigationContainer>
    );
}

export default App;