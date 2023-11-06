import { SafeAreaView, ScrollView, StatusBar, StyleSheet, View, useColorScheme } from "react-native";
import { HomeTabScreenProps } from "../../utils/types";

type ConnectProps = HomeTabScreenProps<"connect">;

const Connect: React.FC<ConnectProps> = ({ navigation, route }) =>{
    const colorScheme = useColorScheme();

    const isDarkMode = colorScheme === 'dark';

    const backgroundStyle = {
        backgroundColor: isDarkMode ? "#0f0f0f" : "white",
    };
    
    return (
        <SafeAreaView style={backgroundStyle}>
            <StatusBar barStyle={ isDarkMode ? 'light-content' : 'dark-content'} backgroundColor={"white"} />
            <ScrollView style={ styles.connect }>

            </ScrollView>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    connect: {}
});

export default Connect;