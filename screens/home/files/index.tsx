import { StyleSheet, Text, View } from "react-native";
import { Dirs } from 'react-native-file-access';
import Browser from "./browser";
import { FilesStackParamList, HomeTabScreenProps } from "../../../utils/types";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import MediaList from "./media";
import List from "./list";

const Stack = createNativeStackNavigator<FilesStackParamList>();

const Files: React.FC<HomeTabScreenProps<"files">> = () =>{
    return (
        <Stack.Navigator>
            <Stack.Screen name="media" component={MediaList} />
            <Stack.Screen name="browser" component={Browser} />
            <Stack.Screen name="list" component={List} />
        </Stack.Navigator>
    );
}

const styles = StyleSheet.create({
    files: {}
});

export default Files;

//<Browser root={Dirs.SDCardDir!} />