import { ScrollView } from "react-native";
import { HomeTabScreenProps } from "../../utils/types";

type SettingsProps = HomeTabScreenProps<"settings">;

const Settings: React.FC<SettingsProps> = ({ navigation, route }) =>{
    return (
        <ScrollView>

        </ScrollView>
    );
}

export default Settings;