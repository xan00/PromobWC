package za.co.rdata.r_datamobile.meterReadingModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import za.co.rdata.r_datamobile.Models.model_pro_mr_no_access;
import za.co.rdata.r_datamobile.Models.model_pro_mr_notes;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_header;

/**
 * Project: R-DataMobile
 * Created by wcrous on 11/12/2015.
 */
public class MeterReaderController {

    public static class Keys {
        String InstNode_id;
        String mobnode_id;
        String cycle;
        String route_number;
        int walk_sequence;
        int meter_id;
        double meter_reading;
        String no_access;

        public double getMeter_reading() {
            return meter_reading;
        }

        public void setMeter_reading(double meter_reading) {
            this.meter_reading = meter_reading;
        }

        public String getNo_access() {
            return no_access;
        }

        public void setNo_access(String no_access) {
            this.no_access = no_access;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        String note;

        public Keys(String cycle, String instNode_id, int meter_id, String mobnode_id, String route_number, int walk_sequence) {
            this.cycle = cycle;
            InstNode_id = instNode_id;
            this.meter_id = meter_id;
            this.mobnode_id = mobnode_id;
            this.route_number = route_number;
            this.walk_sequence = walk_sequence;
        }

        public Keys(String cycle, String instNode_id, int meter_id, String mobnode_id, String route_number, int walk_sequence, double meter_reading, String no_access, String note) {
            this.cycle = cycle;
            InstNode_id = instNode_id;
            this.meter_id = meter_id;
            this.mobnode_id = mobnode_id;
            this.route_number = route_number;
            this.walk_sequence = walk_sequence;
            this.meter_reading = meter_reading;
            this.no_access = no_access;
            this.note = note;
        }

        public static Boolean Equals(Keys Key1, Keys Key2) {
            return (Key1.InstNode_id.compareTo(Key2.InstNode_id) == 0) &&
                    (Key1.mobnode_id.compareTo(Key2.mobnode_id) == 0) &&
                    (Key1.cycle.compareTo(Key2.cycle) == 0) &&
                    (Key1.route_number.compareTo(Key2.route_number) == 0) &&
                    Key1.walk_sequence == Key2.walk_sequence &&
                    Key1.meter_id == Key2.meter_id;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }

        public String getInstNode_id() {
            return InstNode_id;
        }

        public void setInstNode_id(String instNode_id) {
            InstNode_id = instNode_id;
        }

        public int getMeter_id() {
            return meter_id;
        }

        public void setMeter_id(int meter_id) {
            this.meter_id = meter_id;
        }

        public String getMobnode_id() {
            return mobnode_id;
        }

        public void setMobnode_id(String mobnode_id) {
            this.mobnode_id = mobnode_id;
        }

        public String getRoute_number() {
            return route_number;
        }

        public void setRoute_number(String route_number) {
            this.route_number = route_number;
        }

        public int getWalk_sequence() {
            return walk_sequence;
        }

        public void setWalk_sequence(int walk_sequence) {
            this.walk_sequence = walk_sequence;
        }
    }

    public static String RouteNumber;

    public static List<Keys> route_row_keys = new ArrayList<>();
    public static List<model_pro_mr_notes> notes = new ArrayList<>();
    public static List<model_pro_mr_no_access> no_access = new ArrayList<>();
    public static model_pro_mr_route_header route_header;

    public static Set ConvertKeyToSet(MeterReaderController.Keys key) {
        if(key==null)
            return null;

        Set keySet = new HashSet();
        keySet.add("Cycle|" + key.getCycle());
        keySet.add("InstNode_id|" + key.getInstNode_id());
        keySet.add("Mobnode_id|" + key.getMobnode_id());
        keySet.add("Route_number|" + key.getRoute_number());
        keySet.add("Meter_id|" + key.getMeter_id());
        keySet.add("Walk_sequence|" + key.getWalk_sequence());
        return keySet;
    }

    public static MeterReaderController.Keys ConvertSetToKey(Set<String> set) {
        if (set.size() != 6 || set == null)
            return null;

        String[] keySet = set.toArray(new String[5]);
        String cycle = null, instNode = null, mobNode = null, routeNumber = null;
        String[] ss;
        Integer meterID = null, walkSequence = null;

        for (String s : keySet) {
            ss = s.split("\\|");
            if (ss.length != 2)
                return null;

            switch (ss[0]) {
                case "Cycle":
                    cycle = ss[1];
                    break;
                case "InstNode_id":
                    instNode = ss[1];
                    break;
                case "Mobnode_id":
                    mobNode = ss[1];
                    break;
                case "Route_number":
                    routeNumber = ss[1];
                    break;
                case "Meter_id":
                    meterID = Integer.parseInt(ss[1]);
                    break;
                case "Walk_sequence":
                    walkSequence = Integer.parseInt(ss[1]);
                    break;
            }
        }

        if (cycle == null || instNode == null || meterID == null || mobNode == null || routeNumber == null || walkSequence == null)
            return null;
        return new MeterReaderController.Keys(cycle, instNode, meterID, mobNode, routeNumber, walkSequence);
    }
}