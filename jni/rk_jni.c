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

#include "rk_jni.h"

#ifndef _Included_com_ntek_rockchip_jni_RKJNI
#define _Included_com_ntek_rockchip_jni_RKJNI
#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "PLATFORMERS"
#define LOGE(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOG_TRACE(...)

CS6422_data cs6422_data;
DP_DATA dp_data;

int init(char *dev) {
	int fd = open(dev, O_RDWR);
	if (fd <= 0) {
		LOGE("open() Failed\n");
		exit(-1);
	}
	return fd;
}

/*
 /*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    SEledlight
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledlight(JNIEnv *env, jobject obj, jint onOff){
	int fd;
	LOGV("%s\n", __func__);

	fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);return;
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_LED_ON, &cs6422_data) < 0) {
				LOGE("ioctl() Failed\n");return;
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_LED_OFF, &cs6422_data) < 0) {
				LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

/*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    SEledlock
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledlock
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);
		return;
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_RED_LED_ON, &cs6422_data) < 0) {
			LOGE(
		"ioctl() Failed\n");return;
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_RED_LED_OFF, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

/*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    SEledunlock
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledunlock
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);return;
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_GREEN_LED_ON, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_GREEN_LED_OFF, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    SEledonline
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledonline
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_ORANGE_LED_ON, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_ORANGE_LED_OFF, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    SEledflash1
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledflash1
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);return;
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_FLASHEN1_ON, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_FLASHEN1_OFF, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    SEledflash2
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledflash2
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);return;
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_FLASHEN2_ON, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_FLASHEN2_OFF, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    SEledflashboth
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_SEledflashboth
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);fd = init(SMARTENTRY);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);return;
	}

	if(onOff == 1)
	{
		if (ioctl(fd, MCU_FLASHENBOTH_ON, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		if (ioctl(fd, MCU_FLASHENBOTH_OFF, &cs6422_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPledspeaker
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPledspeaker
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);
	fd = init(DOORPAD);

	DP_DATA dp_data;

	if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);return;
	}

	if(onOff == 1)
	{
		dp_data.value=1;
		if (ioctl(fd, SPKRLED_IO, &dp_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		dp_data.value=0;
		if (ioctl(fd, SPKRLED_IO, &dp_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPled1
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPled1
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);
	DP_DATA dp_data;

	fd = init(DOORPAD);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);
		return;
	}

	if(onOff == 1)
	{
		dp_data.value=1;
		if (ioctl(fd, LED1_IO, &dp_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		dp_data.value=0;
		if (ioctl(fd, LED1_IO, &dp_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPled2
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPled2
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	LOGV("%s\n", __func__);
	DP_DATA dp_data;

	fd = init(DOORPAD);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);return;
	}

	if(onOff == 1)
	{
		dp_data.value=1;
		if (ioctl(fd, LED2_IO, &dp_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	else if(onOff == 0)
	{
		dp_data.value=0;
		if (ioctl(fd, LED2_IO, &dp_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DT2led
  * Signature: (Ljava/lang/String;)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2led
   (JNIEnv *env, jobject obj, jstring param){
	int fd;
	led_data dtdrivers_data;
	LOGV("%s called\n", __func__);

	char par[10]; //get string from java
	const char* stringC = (*env)->GetStringUTFChars(env, param, 0); //get from java
	strcpy(par, stringC); //get from java
	(*env)->ReleaseStringUTFChars(env, param, stringC);

	fd = init(DT2LED);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DT2LED);
		return;
	}

	if (strcmp(par, "ON") == 0) {
		if (ioctl(fd, ON, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "FIVE_SEC") == 0) {
		if (ioctl(fd, ON_DELAY, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "SLOW") == 0) {
		if (ioctl(fd, SLOW, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "FAST") == 0) {
		if (ioctl(fd, FAST, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "ONCE") == 0) {
		if (ioctl(fd, ONCE, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "TWICE") == 0) {
		if (ioctl(fd, TWICE, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "RECOVERY") == 0) {
		if (ioctl(fd, RECOVERY, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	} else if (strcmp(par, "OFF") == 0) {
		if (ioctl(fd, OFF, &dtdrivers_data) < 0) {
			LOGE("ioctl() Failed\n");
		}
	}
	close(fd);

}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DT2Relay0Status
  * Signature: ()I
  */
 JNIEXPORT jint JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2Relay0Status
   (JNIEnv *env, jobject obj){
	 int status;
	 int fd;
	 DT2_data dt2_data;

	 LOGV("%s called\n", __func__);

	 fd = init(DOORTALK2);
	 if(fd<=0){
		 LOGV("%s, open() failed!\n", DOORTALK2);
		 return 0;
	 }

	 if (ioctl(fd, DT2_RLY0_STATUS, &dt2_data) < 0) {
		 LOGE("ioctl() Failed\n");
		 return 0;
	 }

	 status = dt2_data.value;
	 close(fd);
	 return status;
 }

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DT2Relay1Status
  * Signature: ()I
  */
 JNIEXPORT jint JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2Relay1Status
   (JNIEnv *env, jobject obj){
	  int status;
	  int fd;
	  DT2_data dt2_data;

	  LOGV("%s called\n", __func__);
	  fd = init(DOORTALK2);

	  if(fd<=0){
		  LOGV("%s, open() failed!\n", DOORTALK2);
		  return 0;
	  }

	  if (ioctl(fd, DT2_RLY1_STATUS, &dt2_data) < 0) {
		  LOGE("ioctl() Failed\n");
		  return 0;
	  }

	  status = dt2_data.value;
	  close(fd);
	  return status;
  }

  /*
   * Class:     com_ntek_rockchip_jni_RKJNI
   * Method:    DT2MotionSensitivity
   * Signature: (I)V
   */
  JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2MotionSensitivity
    (JNIEnv * env, jobject obj, jint sensitivity){
	  int fd;
	  DT2_data dt2_data;

	  LOGV("%s called\n", __func__);
	  fd = init(DOORTALK2);

	  if(fd<=0){
		  LOGV("%s, open() failed!\n", DOORTALK2);
	  	  return;
	  }

	  dt2_data.value = sensitivity;
	  if (ioctl(fd, DT2_MOTION_SENSE, &dt2_data) < 0) {
	  		  LOGE("ioctl() Failed\n");
	  }
	  close(fd);
  }

/*
   * Class:     com_ntek_rockchip_jni_RKJNI
   * Method:    DT2Sensor0Sensitivity
   * Signature: (I)V
*/
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2Sensor0Sensitivity (JNIEnv *env, jobject obj, jint sensitivity){
	  int fd;
	  DT2_data dt2_data;

	  LOGV("%s called\n", __func__);
	  fd = init(DOORTALK2);

	  if(fd<=0){
		  LOGV("%s, open() failed!\n", DOORTALK2);
	  	  return;
	  }

	  dt2_data.value = sensitivity;
	  if (ioctl(fd, DT2_SENSOR0_SENSE, &dt2_data) < 0) {
	  		  LOGE("ioctl() Failed\n");
	  }
	  close(fd);
}

/*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    DT2Sensor1Sensitivity
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2Sensor1Sensitivity
  (JNIEnv *env, jobject obj, jint sensitivity){
	  int fd;
	  DT2_data dt2_data;

	  LOGV("%s called\n", __func__);
	  fd = init(DOORTALK2);

	  if(fd<=0){
		  LOGV("%s, open() failed!\n", DOORTALK2);
	  	  return;
	  }

	  dt2_data.value = sensitivity;
	  if (ioctl(fd, DT2_SENSOR1_SENSE, &dt2_data) < 0) {
	  		  LOGE("ioctl() Failed\n");
	  }
	  close(fd);
}

/*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    DT2DarknessLevel
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2DarknessLevel
  (JNIEnv *env, jobject obj, jint level, jint darkness){
	  int fd;
	  DT2_data dt2_data;

	  LOGV("%s called\n", __func__);
	  fd = init(DOORTALK2);

	  if(fd<=0){
		  LOGV("%s, open() failed!\n", DOORTALK2);
	  	  return;
	  }

	  if(level == 1){
		  dt2_data.value = darkness;
		  if (ioctl(fd, DT2_LDR_SENSE_LOW, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }else if (level == 2){
		  dt2_data.value = darkness;
		  if (ioctl(fd, DT2_LDR_SENSE_MEDIUM, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }else if(level == 3){
		  dt2_data.value = darkness;
		  if (ioctl(fd, DT2_LDR_SENSE_HIGH, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }
	  close(fd);
}

/*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    DT2IRSetLevel
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DT2IRSetLevel
  (JNIEnv *env, jobject obj, jint level){
	  int fd;
	  DT2_data dt2_data;

	  LOGV("%s called\n", __func__);
	  fd = init(DOORTALK2);

	  if(fd<=0){
		  LOGV("%s, open() failed!\n", DOORTALK2);
	  	  return;
	  }

	  if(level == 1){
		  LOGV("IR LEVEL 1\n");
		  if (ioctl(fd, DT2_IR_LEVEL1, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }else if (level == 2){
		  LOGV("IR LEVEL 2\n");
		  if (ioctl(fd, DT2_IR_LEVEL2, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }else if(level == 3){
		  LOGV("IR LEVEL 3\n");
		  if (ioctl(fd, DT2_IR_LEVEL3, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }else if (level == 0){
		  LOGV("IR LEVEL 0\n");
		  if (ioctl(fd, DT2_IR_OFF, &dt2_data) < 0) {
			  LOGE("ioctl() Failed\n");
		  }
	  }
	  close(fd);
}

/*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPspeakerMode
  * Signature: ()V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPspeakerMode
   (JNIEnv *env, jobject obj){
	 int fd;
	 LOGV("%s\n", __func__);

	 fd = init(DOORPAD);
	 if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);
	 }

	DP_DATA dp_data;
	ioctl(fd, SPEAKER_IO, &dp_data);
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPhandsetMode
  * Signature: ()V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPhandsetMode
   (JNIEnv *env, jobject obj){
	int fd;
	LOGV("%s\n", __func__);

	fd = init(DOORPAD);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);
	}

	DP_DATA dp_data;
	ioctl(fd, HANDSET_IO, &dp_data);
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPled1blink
  * Signature: ()V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPled1blink
   (JNIEnv *env, jobject obj){
	int fd;
	LOGV("%s\n", __func__);

	fd = init(DOORPAD);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);
	}

	DP_DATA dp_data;
	ioctl(fd, LED1_BLINK_IO, &dp_data);
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    DPled2blink
  * Signature: ()V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_DPled2blink
   (JNIEnv *env, jobject obj){
	int fd;
	LOGV("%s\n", __func__);

	fd = init(DOORPAD);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DOORPAD);
	}

	DP_DATA dp_data;
	ioctl(fd, LED2_BLINK_IO, &dp_data);
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    relay0
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_relay0
   (JNIEnv *env, jobject obj, jint onOff){
	int fd;

	DT2_data dt2_data;
	DP_DATA dp_data;

	LOGV("%s\n", __func__);
	fd = init(DEV_NAME);
	if(fd<=0){
		LOGV("%s, open() failed!\n", DEV_NAME);
		return;
	}

	if(onOff == 1)
	{
		if (strcmp(DEV_NAME, DOORTALK2)==0) {
			if (ioctl(fd, DT2_RLY0_ON, &dt2_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		} else if (strcmp(DEV_NAME, DOORPAD)==0) {
			if (ioctl(fd, DP_RLY0_ON, &dp_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}else{
			if (ioctl(fd, MCU_RLY0_ON, &cs6422_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}
	}
	else if(onOff == 0)
	{
		if (strcmp(DEV_NAME, DOORTALK2)==0) {
			if (ioctl(fd, DT2_RLY0_OFF, &dt2_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}else if(strcmp(DEV_NAME, DOORPAD)==0){
			if (ioctl(fd, DP_RLY0_OFF, &dp_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}else {
			if (ioctl(fd, MCU_RLY0_OFF, &cs6422_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}
	}
	close(fd);
}

 /*
  * Class:     com_ntek_rockchip_jni_RKJNI
  * Method:    relay1
  * Signature: (I)V
  */
 JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_relay1 (JNIEnv * env, jobject obj, jint onOff){
	int fd;

	DT2_data dt2_data;
	DP_DATA dp_data;

	LOGV("%s\n", __func__);
	fd = init(DEV_NAME);
	if(fd<=0){
		LOGV("%s, open() failed!\n", SMARTENTRY);
	}

	if(onOff == 1)
	{
		if (strcmp(DEV_NAME, DOORTALK2)==0) {
			if (ioctl(fd, DT2_RLY1_ON, &dt2_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		} else if (strcmp(DEV_NAME, DOORPAD)==0) {
			if (ioctl(fd, DP_RLY1_ON, &dp_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		} else {
			if (ioctl(fd, MCU_RLY1_ON, &cs6422_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}
	}
	else if(onOff == 0)
	{
		if (strcmp(DEV_NAME, DOORTALK2)==0) {
			if (ioctl(fd, DT2_RLY1_OFF, &dt2_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		} else if (strcmp(DEV_NAME, DOORPAD)==0) {
			if (ioctl(fd, DP_RLY1_OFF, &dp_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		} else {
			if (ioctl(fd, MCU_RLY1_OFF, &cs6422_data) < 0) {
				LOGE("ioctl() Failed\n");
			}
		}
	}
	close(fd);
}

/*
 * Class:     com_ntek_rockchip_jni_RKJNI
 * Method:    device
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ntek_rockchip_jni_RKJNI_device
(JNIEnv *env, jobject obj, jstring param) {

	char device[20]; //get string from java
	const char* stringC = (*env)->GetStringUTFChars(env, param, 0);//get from java
	strcpy(device, stringC);//get from java
	(*env)->ReleaseStringUTFChars(env, param, stringC);

	LOGE("%s, device: %s\n", __func__, device);

	if(strcmp(device, "SmartEntry")==0) {
		strcpy(DEV_NAME, SMARTENTRY);
	}
	else if (strcmp(device, "DoorPad")==0 || strcmp(device, "Vivex")==0) {
		strcpy(DEV_NAME, DOORPAD);
	}
	else if (strcmp(device, "DoorTalk2")==0) {
		strcpy(DEV_NAME, DOORTALK2);
	}
}


#ifdef __cplusplus
}
#endif
#endif
