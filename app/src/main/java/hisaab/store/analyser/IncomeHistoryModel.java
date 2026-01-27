package hisaab.store.analyser;

public class IncomeHistoryModel {
        public String source;
        public double amount;
        public String date; // format: dd-MM-yyyy

        public IncomeHistoryModel(String source, double amount, String date) {
            this.source = source;
            this.amount = amount;
            this.date = date;
        }
    }
