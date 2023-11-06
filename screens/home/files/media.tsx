import React, { useEffect, useState } from 'react';
import { View, FlatList, Image, Text } from 'react-native';
import { FilesStackScreenProps } from '../../../utils/types';
//import * as MediaLibrary from '@pontusab/react-native-media-library';

const MediaList: React.FC<FilesStackScreenProps<"media">> = () => {
  const [mediaItems, setMediaItems] = useState<any>([]);

  useEffect(() => {
    /*async function fetchMedia() {
      const { assets } = await MediaLibrary.getAssetsAsync({ mediaType: 'photo' });
      setMediaItems(assets);
    }*/

    //fetchMedia();
  }, []);

  return (
    <View style={{ flex: 1 }}>
      <FlatList
        data={mediaItems}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={{ margin: 5 }}>
            <Image source={{ uri: item.uri }} style={{ width: 100, height: 100 }} />
            <Text>{item.filename}</Text>
          </View>
        )}
      />
    </View>
  );
};

export default MediaList;