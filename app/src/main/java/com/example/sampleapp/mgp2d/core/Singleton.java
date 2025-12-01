package com.example.sampleapp.mgp2d.core;

public class Singleton<T> {
    private static final java.util.Map<Class<?>, Singleton<?>> instances = new java.util.HashMap<>();

    @SuppressWarnings("unchecked")
    protected static synchronized <T extends Singleton<T>> T getInstance(Class<T> clazz) {
        if (!instances.containsKey(clazz)) {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
                instances.put(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create singleton instance of " + clazz.getName(), e);
            }
        }
        return (T) instances.get(clazz);
    }

    protected Singleton() {
        // Prevent external construction if an instance already exists
        if (instances.containsKey(this.getClass())) {
            throw new RuntimeException("Instance already exists for: " + this.getClass().getName());
        }
    }
}
