import React, { useCallback, useEffect, useState } from "react";
import { ScrollView, StyleSheet, Text, View } from "react-native";
import { FileSystem } from 'react-native-file-access';
import File, { Fileprops } from "../../../components/file";
import { BrowserNavigation, LoadingWave } from "../../../components";
import { FilesStackScreenProps } from "../../../utils/types";

const Browser: React.FC<FilesStackScreenProps<"browser">> = ({ navigation, route }) =>{
    const [files, setFiles] = useState<Fileprops[]>([]);
    const [current, setCurrent] = useState(route.params.root);
    const [loading, setLoading] = useState(false);

    const fetch =  useCallback(async () => {
        let output: Fileprops[] = [];

        try{
            setLoading(true);
            const files = (await FileSystem.ls(current)).sort((a: string, b: string)=> a.toLocaleLowerCase().localeCompare(b.toLocaleLowerCase()));
            for(let i = 0; i < files.length; i++){
                const file = files[i];
                const stats = await FileSystem.stat(`${current}/${file}`);
                const count = (await FileSystem.ls(`${current}/${file}`)).length;

                output.push({ isDir: stats.type === "directory", name: stats.filename, modified: stats.lastModified, filesCount: count, path: stats.path });
            }
        }catch(error){
            console.error(error);
        }
        setLoading(false);
        return output;
    }, [current]);

    useEffect(()=> { fetch().then(value => setFiles(value)); }, [fetch]);

    const folderClicked = (path: string) =>{
        console.log(path);
        setCurrent(path);
    }

    return (
        <View style={{ flex: 1 }}>
            <BrowserNavigation root={route.params.root} path={current} onPathClick={folderClicked}/>
            { !loading && <ScrollView>
                { files.map((file)=> <File {...file} key={file.path} onClick={folderClicked}/>) }
            </ScrollView> }
            { loading && <View style={style.loadingContainer}>
                <LoadingWave />
            </View>}
        </View>
    );
}

const style = StyleSheet.create({
    loadingContainer: {
        flex: 1,
        alignItems: "center",
        justifyContent: "center"
    }
});

export default Browser;