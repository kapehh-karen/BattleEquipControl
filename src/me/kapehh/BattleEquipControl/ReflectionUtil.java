package me.kapehh.BattleEquipControl;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;

/**
 * @author: mtymes
 * @since: 27.4.2009 0:38:03
 */
public class ReflectionUtil {

    /* ============================ */
    /* ----- field processing ----- */
    /* ============================ */


    public static Field getField(final Class beanClass, final String fieldName) throws NoSuchFieldException {

        Field field = null;

        Class tempClass = beanClass;
        while (field == null && tempClass != null) {
            try {
                field = tempClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                tempClass = tempClass.getSuperclass();
            }
        }

        if (field == null) {
            throw new NoSuchFieldException(fieldName + " in class " + beanClass);
        }

        return field;
    }


    public static Object getFieldValue(Object bean,
                                       String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(bean.getClass(), fieldName);
        boolean accessible = field.isAccessible();
        Object value = null;

        field.setAccessible(true);
        try {
            value = field.get(bean);
        } finally {
            field.setAccessible(accessible);
        }

        return value;
    }


    public static void setFieldValue(final Object bean, final String fieldName,
                                     Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(bean.getClass(), fieldName);
        boolean accessible = field.isAccessible();

        field.setAccessible(true);
        try {
            field.set(bean, newValue);
        } finally {
            field.setAccessible(accessible);
        }
    }


    public static Object getStaticFieldValue(final Class beanClass,
                                             final String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(beanClass, fieldName);
        boolean accessible = field.isAccessible();
        Object value = null;

        field.setAccessible(true);
        try {
            value = field.get(beanClass);
        } finally {
            field.setAccessible(accessible);
        }

        return value;
    }


    /**
     * <b>Experimental and too much related to internal java implementation - use on your own risk</b>
     */
    public static void setStaticFieldValue(final Class beanClass, final String fieldName,
                                           final Object newValue) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // TODO: find some better way how to change static field
        Field field = getField(beanClass, fieldName);
        boolean accessible = field.isAccessible();

        field.setAccessible(true);
        try {
            FieldAccessor fieldAccessor = (FieldAccessor) invokeMethod(field, "getFieldAccessor", new Class[]{
                    Object.class
            }, new Object[]{
                    beanClass
            });

            Object isReadOnly = getFieldValue(fieldAccessor, "isReadOnly");
            try {
                setFieldValue(fieldAccessor, "isReadOnly", false);

//                field.set(beanClass, newValue);
                fieldAccessor.set(beanClass, newValue);
            } finally {
                setFieldValue(fieldAccessor, "isReadOnly", isReadOnly);
            }
        } finally {
            field.setAccessible(accessible);
        }
    }


    /**
     * <b>Experimental - is using class from sun.* package - use on your own risk</b>
     *
     * this code has been inspired by the page: http://www.javaspecialists.eu/archive/Issue161.html
     */
    public static void setStaticFieldValue2(final Class beanClass, final String fieldName,
                                            final Object newValue) throws NoSuchFieldException, IllegalAccessException {
        // TODO: find some better way how to change static field
        Field field = getField(beanClass, fieldName);
        boolean accessible = field.isAccessible();

        field.setAccessible(true);
        try {
            int modifiers = field.getModifiers();

            setFieldValue(field, "modifiers", modifiers & ~Modifier.FINAL);
            try {
                // this code uses class from sun.* package and as of this there is no warranty
                // that it will exist in the next java release
                FieldAccessor fieldAccessor = ReflectionFactory.getReflectionFactory().newFieldAccessor(field, false);
                fieldAccessor.set(null, newValue);
            } finally {
                setFieldValue(field, "modifiers", modifiers);
            }
        } finally {
            field.setAccessible(accessible);
        }
    }

    /* ============================= */
    /* ----- method processing ----- */
    /* ============================= */


    public static Method getMethod(final Class beanClass, final String methodName,
                                   final Class... parameterTypes) throws NoSuchMethodException {

        Method method = null;

        Class tempClass = beanClass;
        while (method == null && tempClass != null) {
            try {
                method = tempClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                tempClass = tempClass.getSuperclass();
                e.printStackTrace();
            }
        }

        if (method == null) {
            throw new NoSuchMethodException();
        }

        return method;
    }


    public static Object invokeMethod(final Object bean, final String methodName, final Class[] parameterTypes,
                                      final Object[] values) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = getMethod(bean.getClass(), methodName, parameterTypes);
        boolean accessible = method.isAccessible();
        Object value = null;

        method.setAccessible(true);
        try {
            value = method.invoke(bean, values);
        } finally {
            method.setAccessible(accessible);
        }

        return value;
    }

    /* ================================== */
    /* ----- constructor processing ----- */
    /* ================================== */


    public static Constructor getConstructor(final Class beanClass,
                                             final Class... parameterTypes) throws NoSuchMethodException {
        return beanClass.getDeclaredConstructor(parameterTypes);
    }


    public static Object invokeConstructor(final Class beanClass, final Class[] parameterTypes,
                                           final Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Constructor constructor = getConstructor(beanClass, parameterTypes);
        boolean accessible = constructor.isAccessible();
        Object value = null;

        constructor.setAccessible(true);
        try {
            value = constructor.newInstance(values);
        } finally {
            constructor.setAccessible(accessible);
        }

        return value;
    }


    /**
     * <b>Experimental and too much related to internal java implementation - use on your own risk</b>
     */
    public static Object invokeEnumConstructor(final Class beanClass, final Class[] parameterTypes,
                                               final Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Constructor constructor = getConstructor(beanClass, parameterTypes);
        boolean accessible = constructor.isAccessible();
        Object value = null;

        constructor.setAccessible(true);
        try {
            ConstructorAccessor constructorAccessor = (ConstructorAccessor) getFieldValue(constructor, "constructorAccessor");
            if (constructorAccessor == null) {
                invokeMethod(constructor, "acquireConstructorAccessor", new Class[0], new Object[0]);
                constructorAccessor = (ConstructorAccessor) getFieldValue(constructor, "constructorAccessor");
            }
            value = constructorAccessor.newInstance(values);
        } finally {
            constructor.setAccessible(accessible);
        }

        return value;
    }
}
