package server.collection;

import common.Vehicle;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VehicleCollection {
    private final CopyOnWriteArrayList<Vehicle> vehicles = new CopyOnWriteArrayList<>();
    private final Instant initTime = Instant.now();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    // Методы изменения данных
    public void add(Vehicle v) {
        writeLock.lock();
        try {
            vehicles.add(v);
        } finally {
            writeLock.unlock();
        }
    }

    public void addList(ArrayList<Vehicle> v) {
        writeLock.lock();
        try {
            vehicles.addAll(v);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            vehicles.clear();
        } finally {
            writeLock.unlock();
        }
    }

    // Методы доступа к данным (для Менеджера)
    public ArrayList<Vehicle> getVehicles() {
        readLock.lock();
        try {
            return new ArrayList<>(vehicles);
        } finally {
            readLock.unlock();
        }
    }

    public int size() {
        readLock.lock();
        try {
            return vehicles.size();
        } finally {
            readLock.unlock();
        }
    }

    public Instant getInitTime() {
        return initTime;
    }

    public boolean isEmpty() {
        readLock.lock();
        try {
            return vehicles.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    public void rmEl(Vehicle v){
        writeLock.lock();
        try {
            vehicles.remove(v);
        } finally {
            writeLock.unlock();
        }
    }

    public List<Long> getAllID(){
        readLock.lock();
        try {
            List<Long> id = new ArrayList<>();
            for (Vehicle v : vehicles){
                id.add(v.getId());
            }
            return id;
        } finally {
            readLock.unlock();
        }
    }

    public Vehicle getVehicleByID(long id){
        readLock.lock();
        try {
            for (Vehicle v : vehicles){
                if (v.getId() == id){
                    return v;
                }
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    public void replaceVehicle(long id, Vehicle vehicle){
        writeLock.lock();
        try {
            int index = (int) id - 1;
            if (index >= 0 && index < vehicles.size()) {
                vehicles.set(index, vehicle);
            }
        } finally {
            writeLock.unlock();
        }
    }
}