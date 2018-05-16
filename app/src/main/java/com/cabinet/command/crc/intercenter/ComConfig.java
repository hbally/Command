package com.cabinet.command.crc.intercenter;



public class ComConfig {

    public enum Backdates {
        BACKDATE_50(50),
        BACKDATE_75(75),
        BACKDATE_110(110),
        BACKDATE_134(134),
        BACKDATE_150(150),
        BACKDATE_200(200),
        BACKDATE_300(300),
        BACKDATE_600(600),
        BACKDATE_1200(1200),
        BACKDATE_1800(1800),
        BACKDATE_2400(2400),
        BACKDATE_4800(4800),
        BACKDATE_9600(9600),
        BACKDATE_19200(19200),
        BACKDATE_38400(38400),
        BACKDATE_57600(57600),
        BACKDATE_115200(115200),
        BACKDATE_230400(230400),
        BACKDATE_460800(460800),
        BACKDATE_500000(500000),
        BACKDATE_576000(576000),
        BACKDATE_921600(921600),
        BACKDATE_1000000(1000000),
        BACKDATE_1152000(1152000),
        BACKDATE_1500000(1500000),
        BACKDATE_2000000(2000000),
        BACKDATE_2500000(2500000),
        BACKDATE_3000000(3000000),
        BACKDATE_3500000(3500000),
        BACKDATE_4000000(4000000);
        private int value;

        Backdates(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum StopBits {
        SHOPLIFTS_1(1),
        SHOPLIFTS_2(2);
        private int value;

        StopBits(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum DataBits {
        DataBits_5(5),
        DataBits_6(6),
        DataBits_7(7),
        DataBits_8(8);
        private int value;

        DataBits(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum Parity {
        PARITY_NONE_NONE(0),//None
        PARITY_NONE_ODD(1),//Odd
        PARITY_NONE_EVEN(2),//Even
        PARITY_NONE_MARK(3),//Mark
        PARITY_NONE_SPACE(4);//Space
        private int value;

        Parity(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum FlowControl {
        FLOW_CONTROL_NONE(0),//None
        FLOW_CONTROL_CTS_OR_RTS(1);//CTS/RTS
        private int value;

        FlowControl(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

}
