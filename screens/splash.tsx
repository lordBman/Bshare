import { Button, Text, View } from "react-native";
import { useNavigation } from '@react-navigation/native';

const Splash = () =>{
    const navigation = useNavigation();

    const toHome = () => navigation.navigate("Home");
    return (
        <View>
            <Button onPress={toHome} title="click me" />
        </View>
    );
}

export default Splash;