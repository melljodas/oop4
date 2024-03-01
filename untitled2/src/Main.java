import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;



interface NotificationStrategy {
    void sendNotification(String message);
}

class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending email notification: " + message);
    }
}

class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending SMS notification: " + message);
    }
}

interface NotificationFactory {
    NotificationStrategy createNotificationStrategy();
}

class EmailNotificationFactory implements NotificationFactory {
    @Override
    public NotificationStrategy createNotificationStrategy() {
        return new EmailNotificationStrategy();
    }
}

class SMSNotificationFactory implements NotificationFactory {
    @Override
    public NotificationStrategy createNotificationStrategy() {
        return new SMSNotificationStrategy();
    }
}

class NotificationContext {
    private NotificationStrategy strategy;

    public NotificationContext(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void sendNotification(String message) {
        strategy.sendNotification(message);
    }
}

interface Observer {
    void update(String message);
}

class EventLogger implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Event logged: " + message);
    }
}

class AuditLogger implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Audit log entry: " + message);
    }
}

class NotificationManager {
    private List<Observer> observers = new ArrayList<>();
    private NotificationFactory factory;

    public NotificationManager(NotificationFactory factory) {
        this.factory = factory;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void sendNotification(String message) {
        NotificationStrategy strategy = factory.createNotificationStrategy();
        strategy.sendNotification(message);
        notifyObservers(message);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose notification method:");
        System.out.println("1. Email");
        System.out.println("2. SMS");

        int choice = scanner.nextInt();
        NotificationFactory factory = null;

        if (choice == 1) {
            factory = new EmailNotificationFactory();
        } else if (choice == 2) {
            factory = new SMSNotificationFactory();
        } else {
            System.out.println("Invalid choice. Exiting...");
            return;
        }

        NotificationManager notificationManager = new NotificationManager(factory);

        notificationManager.addObserver(new EventLogger());
        notificationManager.addObserver(new AuditLogger());

        System.out.println("Enter notification message:");
        scanner.nextLine();
        String message = scanner.nextLine();

        notificationManager.sendNotification(message);

        scanner.close();
    }
}
