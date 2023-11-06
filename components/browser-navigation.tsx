import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/FontAwesome";

export interface BrowserNavigationProps{
    root: string,
    path: string,
    onPathClick?: (path: string) => void
}

const BrowserNavigation: React.FC<BrowserNavigationProps> = ({ root, path, onPathClick }) =>{
    const folders = path.substring(root.length).split("/").filter(value => value.length > 0);

    const homeClicked = () =>{
        if(onPathClick){
            onPathClick(root);
        }
    }

    const pathClick = (index: number) =>{
        if(onPathClick){
            let init = root;
            for(let i = 0; i <= index; i++){
                init += `/${folders[i]}`;
            }
            onPathClick(init);
        }
    }
    return (
        <View style={styles.tools}>
            <TouchableOpacity onPress={homeClicked}>
                <Icon name={"home"} size={20} />
            </TouchableOpacity>
            { folders.map((folder, index)=>{ 
                return (<View style={styles.inner}>
                    <Icon name="angle-left" size={20} />
                    <TouchableOpacity onPress={()=> pathClick(index)}>
                        <Text style={styles.folder}>{` ${folder} `}</Text>
                    </TouchableOpacity>
                </View>)})
            }
        </View>
    );
}

const styles = StyleSheet.create({
    tools: {
        flexDirection: "row",
        alignItems: "center",
        marginHorizontal: 10,
        paddingVertical: 8,
        borderBottomColor:"grey",
        borderBottomWidth: 0.5,
        gap: 6
    },
    inner:{
        flexDirection: "row",
        alignItems: "center",
        gap: 6
    },
    folder:{
        fontSize: 18,
        fontWeight: "700"
    }
});

export default BrowserNavigation;