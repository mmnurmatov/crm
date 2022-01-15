package uz.isd.javagroup.grandcrm.viewController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class MenuController {

    @PreAuthorize("hasAuthority('Warehouse,Warehouses,Warehouse Products,Warehouse Users,Warehouse Expenses,Warehouse Transfers,Contragents')")
    public void warehouses() {
    }

    @PreAuthorize("hasAuthority('Cash,Cashes,Suppliers,Debtors,Transactions,Cache Confirmation')")
    public void cashes() {
    }

    @PreAuthorize("hasAuthority('Company,Companies,Company types,Company statuses')")
    public void companies() {
    }

    @PreAuthorize("hasAuthority('Product,Products,Product Types,Product Categories,Product Units')")
    public void products() {
    }

    @PreAuthorize("hasAuthority('User,Users,Users statuses')")
    public void users() {
    }

    @PreAuthorize("hasAuthority('Directory,Notifications,Notification Users,Notification Data,Notification Template,Notification Types,Permission," +
            "Role,Countries,Areas,Regions,Messages,Message Types,Technical Support,MoneyType,TransactionType,TransactionStatus,Expense Type')")
    public void directories() {
    }

    @PreAuthorize("hasAuthority('Report,Report #1,Report #2')")
    public void reports() {
    }

    @PreAuthorize("hasAuthority('Settings,Archive,System settings')")
    public void settings() {
    }

    @PreAuthorize("hasAuthority('Support,Tickets,FAQ')")
    public void supports() {
    }

    @PreAuthorize("hasAuthority('Marketing')")
    public void marketing() {
    }

    @PreAuthorize("hasAuthority('Supplier, Income, Expense')")
    public void supplier() {
    }

    @PreAuthorize("hasAuthority('Selling, Apartment Owner, Apartment Owner Contract, Apartment Selling Monitor')")
    public void selling() {
    }

    @PreAuthorize("hasAuthority('Production,Productions,Production Users,Production Requests,Production Transfers,Production Conversion,Production Report')")
    public void production() {
    }

}
