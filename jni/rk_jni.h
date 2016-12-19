#ifndef __RK_JNI_H__
#define __RK_JNI_H__

#define SMARTENTRY	"/dev/se_mcu_driver_dev"
#define DOORPAD		"/dev/dp_driver"
#define VIVEX		"/dev/dp_driver"
#define DT2LED		"/dev/dt2_led_driver"
#define DOORTALK2	"/dev/dt2_mcu_driver_dev"

#define SE	1
#define DP	1
#define VX	1
#define DT	1

char DEV_NAME[20];

#if SE
//========================= SMART ENTRY ==============================
typedef struct {
    unsigned int value;
    unsigned int reg_num;
    unsigned int read;
} CS6422_data;
//#define DEV_NAME "/dev/se_mcu_driver_dev"

#define MCU_IOCTL_MAGIC 'o'

#define MCU_RLY0_ON             _IO(MCU_IOCTL_MAGIC,0x01)
#define MCU_RLY0_OFF            _IO(MCU_IOCTL_MAGIC,0x02)
#define MCU_RLY1_ON             _IO(MCU_IOCTL_MAGIC,0x03)
#define MCU_RLY1_OFF            _IO(MCU_IOCTL_MAGIC,0x04)
#define MCU_LED_ON       		_IO(MCU_IOCTL_MAGIC,0x05)
#define MCU_LED_OFF      		_IO(MCU_IOCTL_MAGIC,0x06)
#define MCU_RED_LED_ON 			_IO(MCU_IOCTL_MAGIC,0x07)
#define MCU_RED_LED_OFF			_IO(MCU_IOCTL_MAGIC,0x08)
#define MCU_GREEN_LED_ON  		_IO(MCU_IOCTL_MAGIC,0x09)
#define MCU_GREEN_LED_OFF		_IO(MCU_IOCTL_MAGIC,0x0A)
#define MCU_RLY0_STATUS			_IOR(MCU_IOCTL_MAGIC,0x0B, CS6422_data)
#define MCU_RLY1_STATUS			_IOR(MCU_IOCTL_MAGIC,0x0C, CS6422_data)
#define MCU_FLASHEN1_ON			_IO(MCU_IOCTL_MAGIC,0x0D)
#define MCU_FLASHEN1_OFF		_IO(MCU_IOCTL_MAGIC,0x0E)
#define MCU_FLASHEN2_ON			_IO(MCU_IOCTL_MAGIC,0x0F)
#define MCU_FLASHEN2_OFF		_IO(MCU_IOCTL_MAGIC,0x10)
#define MCU_FLASHENBOTH_ON		_IO(MCU_IOCTL_MAGIC,0x11)
#define MCU_FLASHENBOTH_OFF		_IO(MCU_IOCTL_MAGIC,0x12)
#define MCU_ORANGE_LED_ON		_IO(MCU_IOCTL_MAGIC,0x13)
#define MCU_ORANGE_LED_OFF	_IO(MCU_IOCTL_MAGIC,0x14)

#endif

#if DP
//========================= DOOR PAD / DESK PHONE ==============================
#define DP_MAGIC  'd'

#define MODE_SPEAKER    0
#define MODE_HANDSET    1

typedef struct
{
    int read;
    int value;
    unsigned int reg_num;
}DP_DATA;

int audio_sw_mode=0;    // interchange between handset and speaker when button is pressed
int broadcast_audio_sw=0;    //gpio plunger event will not broadcast
int plunDown_isPressed = 0;
int plunUp_isPressed = 0;
int flag_isPressed=0;
int flag_led1_monitoring = 1;  //disabled when blinking is requested
int flag_led2_monitoring = 1;

typedef struct
{
    int loud_spkr;
    int led2;
    int plunger;
    int led1;
}GPIO_PINS;

#define LOUD_SPKR_IO        _IOW(DP_MAGIC,1,DP_DATA)
#define LED2_IO             _IOW(DP_MAGIC,2,DP_DATA)
#define PLUNGER_IO          _IOW(DP_MAGIC,3,DP_DATA)
#define LED1_IO             _IOW(DP_MAGIC,4,DP_DATA)
#define SPEAKER_IO          _IOW(DP_MAGIC,5,DP_DATA)
#define HANDSET_IO          _IOW(DP_MAGIC,6,DP_DATA)
#define LED1_BLINK_IO       _IOW(DP_MAGIC,7,DP_DATA)
#define LED2_BLINK_IO       _IOW(DP_MAGIC,8,DP_DATA)
#define SPKRLED_IO          _IOW(DP_MAGIC,9,DP_DATA)
#define DP_RLY0_ON         	_IO(DP_MAGIC,10)
#define DP_RLY0_OFF        	_IO(DP_MAGIC,11)
#define DP_RLY1_ON         	_IO(DP_MAGIC,12)
#define DP_RLY1_OFF        	_IO(DP_MAGIC,13)
#define DP_RLY0_STATUS		_IOR(DP_MAGIC,14,DP_DATA)
#define DP_RLY1_STATUS		_IOR(DP_MAGIC,15,DP_DATA)
#define MOTION_EN_REG           0x32

#define READ_REG_BYTE           0xAA
//#define AUDIOSW_REG                  0xBC LED_PHONE_BUTTON_REG
#define AUDIOSW_REG             0xBB
#define MOTION_REG              0xE3
#define LED_PHONE_BUTTON_REG    0xEB
#define POWER_BUT_REG           0x90
//#define LED2_REG            0XE1
#endif

#if DT
//========================= DOORTALK LED ==============================
typedef struct {
	int special_function_register;
	int value;
} led_data;

// SFR OPERATION
#define ON_OPS						1
#define ON_DELAY_OPS				2
#define SLOW_OPS	    			3
#define	 FAST_OPS					4
#define	 ONCE_OPS					5
#define	 TWICE_OPS					6
#define	 RECOVERY_OPS				7
#define	 OFF_OPS 					8

#define ON					_IOW('p', 1, led_data)
#define ON_DELAY			_IOW('p', 2, led_data)
#define SLOW	 			_IOW('p', 3, led_data)
#define FAST				_IOW('p', 4, led_data)
#define ONCE		    	_IOW('p', 5, led_data)
#define TWICE		    	_IOW('p', 6, led_data)
#define RECOVERY	    	_IOW('p', 7, led_data)
#define OFF					_IOW('p', 8, led_data)

//===================================================================

//========================= DOORTALK DRIVER ==============================
typedef struct {
    unsigned int value;
    unsigned int reg_num;
    unsigned int read;
} DT2_data;

#define DT2_IOCTL_MAGIC	'r'

#define DT2_RLY0_ON             _IO(DT2_IOCTL_MAGIC,0x01)
#define DT2_RLY0_OFF            _IO(DT2_IOCTL_MAGIC,0x02)
#define DT2_RLY1_ON             _IO(DT2_IOCTL_MAGIC,0x03)
#define DT2_RLY1_OFF            _IO(DT2_IOCTL_MAGIC,0x04)
#define DT2_MOTION_ON			_IO(DT2_IOCTL_MAGIC,0x05)
#define DT2_MOTION_OFF			_IO(DT2_IOCTL_MAGIC,0x06)
#define DT2_SENSOR0_ON			_IO(DT2_IOCTL_MAGIC,0x07)
#define DT2_SENSOR0_OFF			_IO(DT2_IOCTL_MAGIC,0x08)
#define DT2_SENSOR1_ON			_IO(DT2_IOCTL_MAGIC,0x09)
#define DT2_SENSOR1_OFF			_IO(DT2_IOCTL_MAGIC,0x0A)
#define DT2_RLY0_STATUS			_IOR(DT2_IOCTL_MAGIC,0x0B, DT2_data)
#define DT2_RLY1_STATUS			_IOR(DT2_IOCTL_MAGIC,0x0C, DT2_data)
#define DT2_LDR_ON				_IO(DT2_IOCTL_MAGIC,0x0D)
#define DT2_LDR_OFF				_IO(DT2_IOCTL_MAGIC,0x0E)
#define DT2_IR_ON				_IO(DT2_IOCTL_MAGIC,0x0F)
#define DT2_IR_OFF				_IO(DT2_IOCTL_MAGIC,0x10)
#define DT2_LDR_SENSE_LOW		_IOW(DT2_IOCTL_MAGIC,0x11, DT2_data)
#define DT2_LDR_SENSE_MEDIUM	_IOW(DT2_IOCTL_MAGIC,0x12, DT2_data)
#define DT2_LDR_SENSE_HIGH		_IOW(DT2_IOCTL_MAGIC,0x13, DT2_data)
#define DT2_IR_LEVEL1			_IO(DT2_IOCTL_MAGIC,0x14)
#define DT2_IR_LEVEL2			_IO(DT2_IOCTL_MAGIC,0x15)
#define DT2_IR_LEVEL3			_IO(DT2_IOCTL_MAGIC,0x16)
#define DT2_SENSOR0_SENSE		_IOW(DT2_IOCTL_MAGIC,0x17, DT2_data)
#define DT2_SENSOR1_SENSE		_IOW(DT2_IOCTL_MAGIC,0x18, DT2_data)
#define DT2_MOTION_SENSE		_IOW(DT2_IOCTL_MAGIC,0x19, DT2_data)
//========================================================================
#endif

int init(char *dev);

#endif
