import { ColorSchemeName, useColorScheme as _useColorScheme } from 'react-native';

const useColorScheme = (): NonNullable<ColorSchemeName> =>{
    return _useColorScheme() as NonNullable<ColorSchemeName>;
}

export default useColorScheme;