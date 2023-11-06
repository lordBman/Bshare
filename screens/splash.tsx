import { Button, Text, View } from "react-native";
import { RootStackScreenProps } from "../utils/types";

type SplashProps = RootStackScreenProps<"splash">;

const Splash: React.FC<SplashProps> = ({ navigation, route }) =>{

    const checkPermissions = async() =>{
        /*try{
            let granted = await MediaLibrary.getPermissionsAsync();
            while(!granted){
                granted = await MediaLibrary.requestPermissionsAsync();
            }
            navigation.navigate("Home");
        }catch(error){
            console.error("permission error", error);
        }*/
    }

    return (
        <View>
            <Text>Hello</Text>
        </View>
    );
}

export default Splash;