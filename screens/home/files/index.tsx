import { StyleSheet, Text, View } from "react-native";
import { Dirs } from 'react-native-file-access';
import Browser from "./browser";

const Files = () =>{
    return (
        <Browser root={Dirs.SDCardDir!} />
    );
}

const styles = StyleSheet.create({
    files: {}
});

export default Files;