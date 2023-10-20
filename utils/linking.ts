/**
 * Learn more about deep linking with React Navigation
 * https://reactnavigation.org/docs/deep-linking
 * https://reactnavigation.org/docs/configuring-links
 */

import { LinkingOptions } from '@react-navigation/native';
import { RootStackParamList } from './types';

const LinkingConfig: LinkingOptions<RootStackParamList> = {
    prefixes: ['/'],
    config: {
        screens: {
            Home: {
                path: "home",
                screens: {
                    Connect : 'connect',
                    Files: 'files',
                    Settings: 'settings'
                },
            },
            Splash: 'splash',
            Info: 'info',
        },
    },
};

export default LinkingConfig;
