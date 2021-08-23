package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PointField {

    private PointList[][] field;

    static class PointList {

        private Map<Class, Point> list = new HashMap<>();

        public void add(Point point) {
            list.put(point.getClass(), point);
        }

        public boolean contains(Class<?> filter) {
            return list.containsKey(filter);
        }

        public void remove(Class<?> filter) {
            list.remove(filter);
        }

        public <T> T get(Class<T> filter) {
            return (T) list.get(filter);
        }
    }

    public PointField(int size) {
        field = new PointList[size][];
        for (int x = 0; x < size; x++) {
            field[x] = new PointList[size];
            for (int y = 0; y < size; y++) {
                field[x][y] = new PointList();
            }
        }
    }

    public int size() {
        return field.length;
    }

    public void addAll(List<? extends Point> elements) {
        elements.forEach(this::add);
    }

    public void add(Point point) {
        get(point).add(point);

        point.onChange((from, to) -> {
            get(from).remove(point.getClass());
            get(to).add(to);
        });
    }

    private PointList get(Point point) {
        return field[point.getX()][point.getY()];
    }

    interface Accessor<T> {
        <E extends Point> boolean contains(E element);

        <E extends Point> void remove(E element);

        List<T> all();

        void removeNotIn(List<T> valid);
    }

    public <T extends Point> Accessor<T> of(Class<T> filter) {
        return new Accessor<>() {
            @Override
            public <E extends Point> boolean contains(E element) {
                return get(element).contains(filter);
            }

            @Override
            public <E extends Point> void remove(E element) {
                get(element).remove(filter);
            }

            @Override
            public List<T> all() {
                List<T> result = new LinkedList<>();
                for (int x = 0; x < size(); x++) {
                    for (int y = 0; y < size(); y++) {
                        T element = field[x][y].get(filter);
                        if (element != null) {
                            result.add(element);
                        }
                    }
                }
                return result;
            }

            @Override
            public void removeNotIn(List<T> valid) {
                all().stream()
                        .filter(it -> !valid.contains(it))
                        .forEach(it -> remove(it));
            }
        };
    }
}