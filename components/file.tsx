import { useState } from "react";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/FontAwesome";

export interface Fileprops{ 
    isDir: boolean, filesCount: number,
    name: string, modified: number,
    path: string,
    onClick?: (path: string) => void
}

const File = (props:Fileprops) =>{
    const onclick = () =>{
        if(props.isDir && props.onClick){
            props.onClick(props.path);
        }
    }

    return (
        <TouchableOpacity onPress={onclick} style={styles.container}>
            <Icon name="folder" size={50} color={"#e91e63"} />
            <View>
                <Text style={styles.name}>{props.name}</Text>
                <Text>{props.filesCount} files</Text>
                <Text style={styles.modified}>last modified { new Date(props.modified).toDateString() }</Text>
            </View>
        </TouchableOpacity>
    );
}

const styles = StyleSheet.create({
    container:{
        flexDirection: "row",
        marginHorizontal: 10,
        paddingVertical: 8,
        borderBottomWidth: 0.5,
        borderBottomColor: "grey",
        alignItems: "flex-start",
        gap: 8
    },
    name:{
        fontSize: 18,
        fontWeight: "700",
    },
    modified:{
        fontSize: 14,
        fontWeight: "300",
        letterSpacing: 1.6
    }
});

export default File;