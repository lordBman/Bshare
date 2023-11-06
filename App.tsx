import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import Screens from './screens';

const App = () =>{
    return (
        <NavigationContainer>
            <Screens />
        </NavigationContainer>
    );
}

export default App;