package ch.avocado.share.common;

import ch.avocado.share.service.exceptions.ServiceNotFoundException;
import ch.avocado.share.service.*;
import ch.avocado.share.service.Impl.*;
import ch.avocado.share.service.Mock.FileStorageHandlerMock;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * The service locator is the entry-point to find a service implementation.
 * Every service should be registered here and can be queried by calling {@link #getService(Class)}
 */
public class ServiceLocator {

    private static final Map<Type, Object> services;

    static {
        try {
            services = new HashMap<>();
            registerServices();
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Returns the implementation of the service.
     * @param clazz The required Service class
     * @param <T> The type of the service
     * @return The implemented service
     * @throws ServiceNotFoundException If there is no service with this class.
     */
    public static <T> T getService(Class<T> clazz) throws ServiceNotFoundException {
        if (!services.containsKey(clazz))
            throw new ServiceNotFoundException(clazz.toString(), ServiceLocator.class.toString());

        return (T) services.get(clazz);
    }

    /**
     * Build the service map.
     */
    private static void registerServices() {
        services.put(ISecurityHandler.class, new SecurityHandler());
        services.put(IDatabaseConnectionHandler.class, new DatabaseConnectionHandler());
        services.put(IUserDataHandler.class, new UserDataHandler());
        services.put(IFileStorageHandler.class, new FileStorageHandlerMock());
        services.put(IMailingService.class, new MailingService());
        services.put(IGroupDataHandler.class, new GroupDataHandler());
        services.put(ICategoryDataHandler.class, new CategoryDataHandler());
        services.put(IModuleDataHandler.class, new ModuleDataHandler());
        services.put(ICaptchaVerifier.class, new ReCaptchaVerifier());
        services.put(IAvatarStorageHandler.class, new AvatarFileStorageHandler());
        services.put(IRatingDataHandler.class, new RatingDataHandler());
        try {
            services.put(ISearchEngineService.class, new SolRJService(getService(IDatabaseConnectionHandler.class)));
            services.put(IFileDataHandler.class, new FileDataHandler(getService(ISearchEngineService.class)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceNotFoundException e) {
            e.printStackTrace();
        }
    }
}
