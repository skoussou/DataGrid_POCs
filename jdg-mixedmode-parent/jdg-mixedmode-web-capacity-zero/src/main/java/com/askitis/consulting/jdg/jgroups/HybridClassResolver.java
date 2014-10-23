package com.askitis.consulting.jdg.jgroups;

import java.io.IOException;
import java.lang.reflect.Proxy;

import org.jboss.marshalling.ClassResolver;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.Unmarshaller;

public final class HybridClassResolver implements ClassResolver {

   private ClassLoader classLoader;

   private HybridClassResolver(final ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public static HybridClassResolver getInstance(final ClassLoader classLoader) {
      return new HybridClassResolver(classLoader);
   }

   /** {@inheritDoc} */
   @Override
   public void annotateClass(final Marshaller marshaller, final Class<?> clazz) throws IOException {
      String clazzName = clazz.getName();
      if (clazzName.startsWith("org.infinispan.")) {
         marshaller.writeObject("org.infinispan");
         marshaller.writeObject("main");
		} else if (clazz.getName().startsWith("com.askitis.consulting.rds.")) {
	         marshaller.writeObject("com.askitis.consulting.rds");
	         marshaller.writeObject("main");
      } else {
         marshaller.writeObject(null);
      }
   }

   /** {@inheritDoc} */
   @Override
   public void annotateProxyClass(final Marshaller marshaller, final Class<?> proxyClass) throws IOException {
      marshaller.writeObject(null);
   }

   /** {@inheritDoc} */
   @Override
   public String getClassName(final Class<?> clazz) throws IOException {
      return clazz.getName();
   }

   /** {@inheritDoc} */
   @Override
   public String[] getProxyInterfaces(final Class<?> proxyClass) throws IOException {
      final Class<?>[] interfaces = proxyClass.getInterfaces();
      final String[] names = new String[interfaces.length];
      for (int i = 0; i < interfaces.length; i++) {
         names[i] = getClassName(interfaces[i]);
      }
      return names;
   }

   /** {@inheritDoc} */
   @Override
   public Class<?> resolveClass(final Unmarshaller unmarshaller, final String className, final long serialVersionUID)
         throws IOException, ClassNotFoundException {
      final String name = (String) unmarshaller.readObject();
      if (name == null) {
         return Class.forName(className, false, classLoader);
      }
      final String slot = (String) unmarshaller.readObject();

      return Class.forName(className, false, classLoader);
   }

   /** {@inheritDoc} */
   @Override
   public Class<?> resolveProxyClass(final Unmarshaller unmarshaller, final String[] names) throws IOException,
         ClassNotFoundException {
      final String name = (String) unmarshaller.readObject();
      if (name != null) {
         final String slot = (String) unmarshaller.readObject();
      }
      final int len = names.length;
      final Class<?>[] interfaces = new Class<?>[len];
      for (int i = 0; i < len; i++) {
         interfaces[i] = Class.forName(names[i], false, classLoader);
      }
      return Proxy.getProxyClass(classLoader, interfaces);
   }
}
