import { BottomTabScreenProps } from '@react-navigation/bottom-tabs';
import { CompositeScreenProps, NavigatorScreenParams } from '@react-navigation/native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';

export type FilesStackParamList = {
  media: undefined,
  browser: { root: string },
  list: { fileType: string },
}

export type FilesStackScreenProps<Screen extends keyof FilesStackParamList> = CompositeScreenProps<NativeStackScreenProps<FilesStackParamList, Screen>,
    CompositeScreenProps<BottomTabScreenProps<HomeTabParamList>, NativeStackScreenProps<RootStackParamList>>>;

export type HomeTabParamList = {
    connect: undefined;
    files: NavigatorScreenParams<FilesStackParamList> | undefined;
    settings: undefined;
};

export type HomeTabScreenProps<Screen extends keyof HomeTabParamList> = CompositeScreenProps<BottomTabScreenProps<HomeTabParamList, Screen>, NativeStackScreenProps<RootStackParamList>>;

export type RootStackParamList = {
  home: NavigatorScreenParams<HomeTabParamList> | undefined;
  splash: undefined;
  info: undefined;
};

export type RootStackScreenProps<Screen extends keyof RootStackParamList> = NativeStackScreenProps<RootStackParamList, Screen>;

