//
// Created by ajaasazeez on 9/7/2021.
//

#include <jni.h>
#include <string>
#include <jni.h>


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_tweetmap_data_DataRepository_getAPIKey(JNIEnv *env, jobject /*thiz*/) {
    std::string apiKey = "2CqQHeK3AWV1cQa4IPusr5I3y";
    return env->NewStringUTF(apiKey.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_tweetmap_data_DataRepository_getAPISecretKey(JNIEnv *env, jobject /*thiz*/) {
    std::string apiKey = "0w7HEluNyzE6z6N6q3EmCC3HdKl75g0veZ0M1QEQlMhnfViy7b";
    return env->NewStringUTF(apiKey.c_str());
}
