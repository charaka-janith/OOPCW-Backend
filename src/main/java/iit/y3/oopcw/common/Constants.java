package iit.y3.oopcw.common;

public class Constants {
    public enum Status {
        SUCCESS("00", "Success"), FAILURE("01", "Failure"), TECHNICAL_ISSUE("02", "Rejected due to a technical issue."), API_ERROR("64", "Error occurred while connection API.");

        private final String code;
        private final String desc;

        private Status(String code, String description) {
            this.code = code;
            this.desc = description;
        }

        public String val() {
            return this.code;
        }

        public String description() {
            return this.desc;
        }
    }
}