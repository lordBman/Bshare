import React, { useEffect, useRef } from 'react';
import { View, Animated, Easing, StyleSheet } from 'react-native';

const LoadingWave = () => {
    const animation = (value: Animated.Value, delay: number) => {
        Animated.loop(
            Animated.sequence([
                Animated.timing(value, {
                    toValue: 0,
                    duration: delay,
                    easing: Easing.sin,
                    useNativeDriver: true,
                }),
                Animated.timing(value, {
                    toValue: 1,
                    duration: 240,
                    easing: Easing.ease,
                    useNativeDriver: true,
                }),
                Animated.timing(value, {
                    toValue: 0,
                    duration: 240,
                    easing: Easing.quad,
                    useNativeDriver: true,
                }),
            ]),
        { iterations: -1,}).start();
    };

    const circleAnimationOne = useRef(new Animated.Value(0)).current;
    const animatedStylesOne = [ styles.circle, {
        transform: [
            { translateY: circleAnimationOne.interpolate({ inputRange: [0, 1],outputRange: [0, -12]})}
        ] }
    ];

    const circleAnimationTwo = useRef(new Animated.Value(0)).current;
    const animatedStylesTwo = [ styles.circle, {
        transform: [
            { translateY: circleAnimationTwo.interpolate({ inputRange: [0, 1],outputRange: [0, -12]})}
        ] }
    ];

    const circleAnimationThree = useRef(new Animated.Value(0)).current;
    const animatedStylesThree = [ styles.circle, {
        transform: [
            { translateY: circleAnimationThree.interpolate({ inputRange: [0, 1],outputRange: [0, -12]})}
        ] }
    ];
    
    useEffect(() => {
        setTimeout(()=> animation(circleAnimationOne, 1000), 0);
        setTimeout(()=> animation(circleAnimationTwo, 1000), 180);
        setTimeout(()=> animation(circleAnimationThree, 1000), 360);
    }, [circleAnimationOne, circleAnimationTwo, circleAnimationThree]);
    
    return (
        <View style={styles.container}>
            <Animated.View style={animatedStylesOne} />
            <Animated.View style={animatedStylesTwo} />
            <Animated.View style={animatedStylesThree} />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
    },
    circle: {
        width: 10,
        height: 10,
        borderRadius: 5,
        backgroundColor: '#e91e63',
        margin: 3,
    },
});

export default LoadingWave;