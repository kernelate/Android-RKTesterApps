/*
 ========================PROJECT_PLATFORMERS==========================
 PROJECT: LED
 AUTHORS: KENNETH UY & RANDY AGUINALDO w/ the help of MASTER KERMITH
 FUNCTIONS: getGPSTime & setLEDtime
 DATE COMPLETED: JUNE 24, 2014

 NOTES:
 GPS Configuration:
 - serial port "/dev/s3c2410_serial0"
 - baud 9600

 LED Configuration:
 - serial port "/dev/s3c2410_serial3"
 - baud 57600

 *We get the value from GPS through "GPGGA"
 *And we use 52 bytes to set the time in LED
 =====================================================================
 */
#include <jni.h>
#include <string.h>
#include <android/log.h>
//#include <utils/Log.h>

//#include "jniHelp.h"
#include <stdio.h>
#include <stdlib.h>

#include <sys/ioctl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>

#include "led_driver.h"

#ifndef _Included_com_ntek_doortalk2_led_Doortalkled
#define _Included_com_ntek_doortalk2_led_Doortalkled
#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "PLATFORMERS"
#define LOGE(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOG_TRACE(...)

#define DEVICE_NAME	"/dev/dt2_led_driver"

#define DEBUG_VERBOSE   0

#define RELAY_ON   			1
#define RELAY_OFF  			0
#define MOTION_ENABLE 		1
#define MOTION_DISABLE 		0

int fd = 0;
led_data dtdrivers_data;

int init() {
//	LOGI("init() \n");
	int ret = 1;
	fd = open(DEVICE_NAME, O_RDWR);
	if (fd <= 0) {
		LOGE("open() Failed\n");
		ret = -1;
		return ret;
//		exit(-1);
	}
	return ret;
}

void deinit() {
//	LOGI("%s called\n", __func__);
	close(fd);
}

int evaluate_operation(int operation, int data) {
	int ret = 1;
//	LOGI("%s called\n", __func__);

//	LOGI("operation = %d\n", operation);

	switch (operation) {
	case ON_OPS:
		LOGV("ON_OPS\n");
		if (ioctl(fd, ON, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case ON_DELAY_OPS:
		LOGV("ON_DELAY_OPS\n");
		if (ioctl(fd, ON_DELAY, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case SLOW_OPS:
		LOGV("SLOW_OPS\n");
		if (ioctl(fd, SLOW, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case FAST_OPS:
		LOGV("FAST_OPS\n");
		if (ioctl(fd, FAST, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case ONCE_OPS:
		LOGV("NOTIFY_OPS\n");
		if (ioctl(fd, ONCE, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case TWICE_OPS:
		LOGV("NOTIFY_OPS\n");
		if (ioctl(fd, TWICE, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case RECOVERY_OPS:
		LOGV("NOTIFY_OPS\n");
		if (ioctl(fd, RECOVERY, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	case OFF_OPS:
		LOGV("OFF_OPS\n");
		if (ioctl(fd, OFF, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
		break;

	}

	return ret;

}

/*
 * Class:     com_ntek_doortalk2_led_Doortalkled
 * Method:    setButtonLED
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_ntek_doortalk2_led_Doortalkled_setButtonLED(
		JNIEnv *env, jobject obj, jstring param) {

	LOGV("%s called\n", __func__);

	char par[10]; //get string from java
	const char* stringC = (*env)->GetStringUTFChars(env, param, 0); //get from java
	strcpy(par, stringC); //get from java
	(*env)->ReleaseStringUTFChars(env, param, stringC);

//	LOGV("%s\n", par);
	int operation = 0;
	int ret;
	jstring return_string; //return string to java

//	LOGV("LED INDICATOR STATUS\n");

	ret = init();

	if (ret < 0)
		return ret;

	if (strcmp(par, "ON") == 0) {
//		operation = check_valid_parameters(1);
		evaluate_operation(1, 0);
	} else if (strcmp(par, "FIVE_SEC") == 0) {
		//		operation = check_valid_parameters(1);
		evaluate_operation(2, 0);
	} else if (strcmp(par, "SLOW") == 0) {
//		operation = check_valid_parameters(2);
		evaluate_operation(3, 0);
	} else if (strcmp(par, "FAST") == 0) {
//		operation = check_valid_parameters(3);
		evaluate_operation(4, 0);
	} else if (strcmp(par, "ONCE") == 0) {
//		operation = check_valid_parameters(4);
		evaluate_operation(5, 0);
	} else if (strcmp(par, "TWICE") == 0) {
		//		operation = check_valid_parameters(4);
		evaluate_operation(6, 0);
	} else if (strcmp(par, "RECOVERY") == 0) {
		//		operation = check_valid_parameters(4);
		evaluate_operation(7, 0);
	} else if (strcmp(par, "OFF") == 0) {
//		operation = check_valid_parameters(6);
		evaluate_operation(8, 0);
	} else {
		ret = 0;
//		LOGV("ret = %d\n", ret);
	}
//	evaluate_operation(operation);
	deinit();
//	LOGI("ret = %d\n", ret);
	return ret;
}

#ifdef __cplusplus
}
#endif
#endif
