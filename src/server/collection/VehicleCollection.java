package server.collection;

import common.Vehicle;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

    public class VehicleCollection {
        private static final ArrayList<Vehicle> vehicles = new ArrayList<>();
        private final Instant initTime = Instant.now();

        // Методы изменения данных
        public void add(Vehicle v) {
            vehicles.add(v);
        }

        public void addList(List<Vehicle> v) {
            vehicles.addAll(v);
        }

        public void clear() {
            vehicles.clear();
        }

        // Методы доступа к данным (для Менеджера)
        public List<Vehicle> getVehicles() {
            return vehicles;
        }

        public int size() {
            return vehicles.size();
        }

        public Instant getInitTime() {
            return initTime;
        }

        public boolean isEmpty() {
            return vehicles.isEmpty();
        }

        public void rmEl(Vehicle v){
            vehicles.remove(v);

        }
        public List getAllID(){
            List<Long> id = new ArrayList<>();
            for (Vehicle v : vehicles){
                id.add(v.getId());
            }
            return id;
        }

//        public void changeID(long previd, long newid) {
//                getVehicleByID(newid).setId(previd);
//        }


        public Vehicle getVehicleByID(long id){
            for (Vehicle v : vehicles){
                if (v.getId() == id){
                    return v;
                }
            }
            return null;
        }
        public void replaceVehicle(long id, Vehicle vehicle){
            vehicles.set((int) id - 1 , vehicle);
        }



    }