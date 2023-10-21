import React, { useCallback, useEffect, useState } from "react";
import LoaderKit from 'react-native-loader-kit'
import { ScrollView, StyleSheet, Text, View } from "react-native";
import { FileSystem } from 'react-native-file-access';
import File, { Fileprops } from "../../../components/file";
import { BrowserNavigation } from "../../../components";

export interface BrowserProps{
    root: string,
}

const Browser: React.FC<BrowserProps> = (props: BrowserProps) =>{
    const [files, setFiles] = useState<Fileprops[]>([]);
    const [current, setCurrent] = useState(props.root);
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
        <View>
            <BrowserNavigation root={props.root} path={current} onPathClick={folderClicked}/>

            { !loading && <ScrollView>
                { files.map((file)=> <File {...file} key={file.path} onClick={folderClicked}/>) }
            </ScrollView> }
            { loading && <View>
                <LoaderKit style={{ width: 50, height: 50 }} name={'BallTrianglePath'} size={50} color={'red'} />
            </View>}
        </View>
    );
}

export default Browser;