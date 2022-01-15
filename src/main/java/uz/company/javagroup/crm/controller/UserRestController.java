package uz.isd.javagroup.grandcrm.controller;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.*;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.PermissionRepository;
import uz.isd.javagroup.grandcrm.repository.directories.RoleRepository;
import uz.isd.javagroup.grandcrm.repository.modules.MonthlyConversionItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.ProductionExpenseRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestRepository;
import uz.isd.javagroup.grandcrm.services.directories.*;
import uz.isd.javagroup.grandcrm.services.modules.*;
import uz.isd.javagroup.grandcrm.utility.ReportOneDto;
import uz.isd.javagroup.grandcrm.utility.SecurityUtility;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.math.BigDecimal.ZERO;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.*;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentStatus.*;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentType.NAKLADNOY;

@RestController
@RequestMapping("/rest")
public class UserRestController extends BaseController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AreaService areaService;
    private final RegionService regionService;
    private final PermissionService permissionService;
    private final WarehouseService warehouseService;
    private final WarehouseExpenseService warehouseExpenseService;
    private final WarehouseProductService warehouseProductService;
    private final WarehouseRequestRepository warehouseRequestRepository;
    private final WarehouseRequestItemRepository warehouseRequestItemRepository;
    private final ProductService productService;
    private final WarehouseProductItemService warehouseProductItemService;
    private final ItemStatusService itemStatusService;
    private final ItemBalanceService itemBalanceService;
    private final ProductCategoryService productCategoryService;
    private final ProductUnitService productUnitService;
    private final ProductTypeService productTypeService;
    private final ContragentService contragentService;
    private final ConversionService conversionService;
    private final ConversionItemService conversionItemService;
    private final MonthlyConversionService monthlyConversionService;
    private final ProductionExpenseRepository productionExpenseRepository;
    private final MonthlyConversionItemRepository monthlyConversionItemRepository;
    private final ExpenseTypeService expenseTypeService;

    public UserRestController(UserService userService,
                              PermissionService permissionService,
                              RoleRepository roleRepository,
                              PermissionRepository permissionRepository,
                              AreaService areaService,
                              RegionService regionService,
                              WarehouseService warehouseService,
                              WarehouseExpenseService warehouseExpenseService,
                              WarehouseProductService warehouseProductService,
                              WarehouseRequestRepository warehouseRequestRepository, WarehouseRequestItemRepository warehouseRequestItemRepository, ProductService productService,
                              WarehouseProductItemService warehouseProductItemService,
                              ItemStatusService itemStatusService,
                              ItemBalanceService itemBalanceService,
                              ProductCategoryService productCategoryService,
                              ProductUnitService productUnitService,
                              MonthlyConversionItemRepository monthlyConversionItemRepository,
                              ProductTypeService productTypeService, ContragentService contragentService, ConversionService conversionService, ConversionItemService conversionItemService, MonthlyConversionService monthlyConversionService, ProductionExpenseRepository productionExpenseRepository, ExpenseTypeService expenseTypeService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.areaService = areaService;
        this.regionService = regionService;
        this.permissionService = permissionService;
        this.warehouseService = warehouseService;
        this.warehouseExpenseService =warehouseExpenseService;
        this.warehouseProductService = warehouseProductService;
        this.warehouseRequestRepository = warehouseRequestRepository;
        this.warehouseRequestItemRepository = warehouseRequestItemRepository;
        this.productService = productService;
        this.warehouseProductItemService = warehouseProductItemService;
        this.itemStatusService = itemStatusService;
        this.itemBalanceService = itemBalanceService;
        this.productCategoryService = productCategoryService;
        this.productUnitService = productUnitService;
        this.productTypeService = productTypeService;
        this.contragentService = contragentService;
        this.conversionService = conversionService;
        this.conversionItemService = conversionItemService;
        this.monthlyConversionService = monthlyConversionService;
        this.productionExpenseRepository = productionExpenseRepository;
        this.expenseTypeService = expenseTypeService;
        this.monthlyConversionItemRepository = monthlyConversionItemRepository;
    }

    @RequestMapping(path = "/get-photo-url", method = POST)
    public Map<String, Object> photoUploadUrl(@RequestParam(name = "productId") Long productId) throws RecordNotFoundException {

        Product product = productService.getProductById(productId);

        String photoUrl = "/product-photos/" + product.getModifiedPhotoName();

        Map<String, Object> map = new HashMap<>();

        if (product.getModifiedPhotoName() == null) {
            map.put("photoUrl", "");
            map.put("status", Boolean.FALSE);
        } else {
            map.put("photoUrl", photoUrl);
            map.put("status", Boolean.TRUE);
        }
        return map;
    }

    @RequestMapping(path = "/check-user-email", method = POST)
//    @PreAuthorize("hasAuthority('Check User Email')")
    public Map<String, Boolean> checkUserEmail(@RequestParam(name = "email") String email) {
        Map<String, Boolean> map = new HashMap<>();
        User user = userService.getUserByEmail(email);
        if (user != null) map.put("status", true);
        else map.put("status", false);
        return map;
    }

    @RequestMapping(path = "/check-username", method = POST)
//    @PreAuthorize("hasAuthority('Check Username')")
    public Map<String, Boolean> checkUsername(@RequestParam(name = "username") String username) {
        Map<String, Boolean> map = new HashMap<>();
        User user = userService.getUserByUsername(username);
        if (user != null) map.put("status", true);
        else map.put("status", false);
        return map;
    }

    @RequestMapping(path = "/check-user-phone", method = POST)
//    @PreAuthorize("hasAuthority('Check User Phone')")
    public Map<String, Boolean> checkUserPhone(@RequestParam(name = "phoneNumber") String phoneNumber) {
        Map<String, Boolean> map = new HashMap<>();
        User user = userService.getUserByPhoneNumber(phoneNumber);
        if (user != null) map.put("status", true);
        else map.put("status", false);
        return map;
    }

    @RequestMapping(path = "/check-password", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Boolean> checkPassword(@RequestParam(name = "oldPassword") String oldPassword) {
        Map<String, Boolean> map = new HashMap<>();
        User user = this.getAuthUserData();
        if (SecurityUtility.passwordEncoder().encode(oldPassword).equals(user.getPassword()))
            map.put("status", Boolean.TRUE);
        else map.put("status", Boolean.FALSE);
        return map;
    }

    @RequestMapping(path = "/by-country-id", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> byCountryId(@RequestParam(name = "countryId") Long countryId) {
        List<Area> areas = areaService.byCountryId(countryId);
        Map<String, Object> map = new HashMap<>();
        if (areas == null || areas.size() == 0) {
            map.put("areas", new ArrayList<>());
            map.put("status", Boolean.FALSE);
        } else {
            map.put("areas", areas);
            map.put("status", Boolean.TRUE);
        }
        return map;
    }

    @RequestMapping(path = "/by-area-id", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> byAreaId(@RequestParam(name = "areaId") Long areaId) {
        List<Region> regions = regionService.byAreaId(areaId);
        Map<String, Object> map = new HashMap<>();
        if (regions == null || regions.size() == 0) {
            map.put("regions", new ArrayList<>());
            map.put("status", Boolean.FALSE);
        } else {
            map.put("regions", regions);
            map.put("status", Boolean.TRUE);
        }
        return map;
    }

    @RequestMapping(path = "/menu-by-role-id", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> menuPermissionByRoleId(@RequestParam(name = "roleId") Long roleId) {
        Map<String, Object> mapResponse = new LinkedHashMap<>();

        List<Object[]> group = permissionService.findAllByMenuNameAndParentIdIsNull(roleId);
        List<Object[]> list = new ArrayList<>(group.size());

        for (Object[] o : group) {
            Permission permission = (Permission) o[0];
            List<Object[]> children;
            children = permissionService.findAllByParentId(roleId, permission.getId());
            for (Object[] obj : children) {
                Permission childPermission = (Permission) obj[0];
                int isSelected = roleRepository.checkPermissionIsSelected(roleId, childPermission.getId());
                childPermission.setIsSelected(isSelected);
                obj[0] = childPermission;
            }
            if (children == null || children.size() == 0) {
                children = new ArrayList<>();
            }
            permission.setChildren(children);
            int isSelected = roleRepository.checkPermissionIsSelected(roleId, permission.getId());
            permission.setIsSelected(isSelected);
            o[0] = permission;
            list.add(o);
        }

        Optional<Role> role = roleRepository.findById(roleId);
        mapResponse.put("role", role);
        mapResponse.put("group", list);

        return mapResponse;
    }


    @RequestMapping(path = "/action-by-role-id", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> ActionPermissionByRoleId(@RequestParam(name = "roleId") Long roleId) {

        List<Permission> group = permissionService.findAllByActionName();

        Map<String, List<Permission>> mapGroup = new LinkedHashMap<>();
        Map<String, Object> mapResponse = new LinkedHashMap<>();

        for (Permission permission : group) {
            String key = permission.getType();
            if (key == null || key.equals("")) {
                key = "Undefined";
            }
            List<Permission> dto;
            if (mapGroup.containsKey(key)) {
                dto = mapGroup.get(key);
            } else {
                dto = new ArrayList<>();
                mapGroup.put(key, dto);
            }

            int isSelected = roleRepository.checkPermissionIsSelected(roleId, permission.getId());
            permission.setIsSelected(isSelected);
            dto.add(permission);

        }
        Optional<Role> role = roleRepository.findById(roleId);
        mapResponse.put("role", role);
        mapResponse.put("group", mapGroup);

        return mapResponse;
    }


    @RequestMapping(path = "/update-perm-by-role", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> updatePermByRole(@RequestParam(name = "roleId") Long roleId,
                                                @RequestParam(name = "perms") List<Long> permIds) throws RecordNotFoundException {

        permissionService.deleteOldConnections(roleId);

        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) throw new RecordNotFoundException("No Role record exists for id: " + roleId);
        permIds.forEach(permId -> {
            Optional<Permission> permission = permissionRepository.findById(permId);
            permission.ifPresent(value -> role.get().getPermissions().add(value));
        });
        roleRepository.save(role.get());
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/update-menu-perm-by-role", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> updatePermMenuByRole(@RequestParam(name = "roleId") Long roleId,
                                                    @RequestParam(name = "perms") List<Long> permIds) throws RecordNotFoundException {

        permissionService.deleteOldMenuConnections(roleId);

        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) throw new RecordNotFoundException("No Role record exists for id: " + roleId);
        permIds.forEach(permId -> {
            Optional<Permission> permission = permissionRepository.findById(permId);
            permission.ifPresent(value -> role.get().getPermissions().add(value));
        });
        roleRepository.save(role.get());
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/generateRegNumber", method = POST)
//    @PreAuthorize("hasAuthority('get warehouse-document')")
    public WarehouseProduct createRegNumber(@RequestParam(name = "warehouseAction") WarehouseProduct.Action action,
                                            @RequestParam(name = "warehouseId") Optional<Long> warehouseId) throws Throwable {
        if (!warehouseId.isPresent()) throw new RecordNotFoundException("Warehouse id is not valid!");
        WarehouseProduct w = new WarehouseProduct();
        w.setAction(action);
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId.get());
        if (action.equals(WarehouseProduct.Action.KIRIM)) {
            w.setToWarehouse(warehouse);
        } else if (action.equals(KOCHIRISH) || action.equals(SPISAT) || action.equals(CHIQIM)) {
            w.setFromWarehouse(warehouse);
        }
        Utils.generateRegNumber(w, warehouseProductService);
        w.setAction(null);
        w.setToWarehouse(null);
        w.setFromWarehouse(null);
        return w;
    }


    @RequestMapping(path = "/getAllContragent", method = POST)
    public Map<String, Object> getAllContragent() throws Throwable {

        List<Contragent> contragents = contragentService.findAll();

        Map<String, Object> map = new HashMap<>();
        if (contragents == null || contragents.size() == 0) {
            map.put("contragents", new ArrayList<>());
            map.put("status", Boolean.FALSE);
        } else {
            map.put("contragents", contragents);
            map.put("status", Boolean.TRUE);
        }
        return map;
    }


    @RequestMapping(path = "/generateRegNumberForRequest", method = POST)
//    @PreAuthorize("hasAuthority('get warehouse-document')")
    public WarehouseRequest createRegNumberForRequest(@RequestParam(name = "warehouseId") Optional<Long> warehouseId) throws Throwable {
        if (!warehouseId.isPresent()) throw new RecordNotFoundException("Warehouse id is not valid!");
        WarehouseRequest w = new WarehouseRequest();
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId.get());
        w.setFromWarehouse(warehouse);
        Utils.generateRegNumber(w, warehouseRequestRepository);
        return w;
    }

    @RequestMapping(path = "/search-by-keyword", method = POST)
//    @PreAuthorize("hasAuthority('get warehouse-document')")
    public List<Product> searchAllColumns(@RequestParam(name = "keyword") Optional<String> keyword) {
        List<Product> products;
        if (keyword.isPresent() && !keyword.get().equals(""))
            products = productService.allByColumn(keyword.get().toLowerCase(), 20);
        else products = productService.all(50);
        return products;
    }

    @RequestMapping(path = "/search-by-keyword-by-warehouse", method = POST)
//    @PreAuthorize("hasAuthority('get warehouse-document')")
    public List<Object[]> searchAllColumnsByWarehouse(@RequestParam(name = "keyword") Optional<String> keyword,
                                                      @RequestParam(name = "fromWarehouseId") Optional<Long> warehouseId) {
        List<Object[]> objects;
        if (keyword.isPresent() && !keyword.get().equals(""))
            objects = itemStatusService.allByColumnByWarehouse(warehouseId.get(), keyword.get().toLowerCase(), 20);
        else objects = itemStatusService.allByWarehouse(warehouseId.get(), 50);
        return objects;
    }

    /**
     * KIRIM store Product without confirmation
     */
    @RequestMapping(path = "/warehouseProductCreate", method = POST)
    @Transactional
    public Map<String, Object> createWarehouseProduct(@RequestParam("createdAt") Date createdAt,
                                                      @RequestParam("documentType") WarehouseProduct.DocumentType documentType,
                                                      @RequestParam("reason") Optional<String> reason,
                                                      @RequestParam("warehouseId") Long warehouseId,
                                                      @RequestParam("action") WarehouseProduct.Action action,
                                                      @RequestParam("regNumber") String regNumber,
                                                      @RequestParam("documentStatus") WarehouseProduct.DocumentStatus documentStatus,
                                                      @RequestParam("contragentId") Optional<Long> contragentId,
                                                      @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {

        WarehouseProduct warehouseProduct = new WarehouseProduct();

        if (contragentId.isPresent())
            warehouseProduct.setContragent(contragentService.getContragentById(contragentId.get()));

        warehouseProduct.setCreatedAt(createdAt);
        reason.ifPresent(warehouseProduct::setReason);
        warehouseProduct.setAction(action);
        warehouseProduct.setDocumentType(documentType);
        warehouseProduct.setRegNumber(regNumber);
        warehouseProduct.setToWarehouse(warehouseService.getWarehouseById(warehouseId));
        warehouseProduct.setDocumentStatus(documentStatus);
        warehouseProduct.setDocumentStatus(READY);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        if (whItems != null && whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProduct);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }
        itemStatusService.countIncomeWhDoc(warehouseProduct);
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/warehouseProductTransferCreate", method = POST)
    @Transactional
    public Map<String, Object> createWarehouseProductTransfer(@RequestParam("createdAt") Date createdAt,
                                                              @RequestParam("documentType") WarehouseProduct.DocumentType documentType,
                                                              @RequestParam("reason") String reason,
                                                              @RequestParam("fromWarehouseId") Long fromWarehouseId,
                                                              @RequestParam("toWarehouseId") Long toWarehouseId,
                                                              @RequestParam("regNumber") String regNumber,
                                                              @RequestParam("documentStatus") WarehouseProduct.DocumentStatus documentStatus,
                                                              @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {
        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setCreatedAt(createdAt);
        warehouseProduct.setReason(reason);
        warehouseProduct.setAction(KOCHIRISH);
        warehouseProduct.setDocumentType(documentType);
        warehouseProduct.setRegNumber(regNumber);
        warehouseProduct.setFromWarehouse(warehouseService.getWarehouseById(fromWarehouseId));
        warehouseProduct.setToWarehouse(warehouseService.getWarehouseById(toWarehouseId));
        warehouseProduct.setDocumentStatus(documentStatus);
        warehouseProduct.setDocumentStatus(WAITING);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        if (whItems != null && whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProduct);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }


    @RequestMapping(path = "/transferWaitingToReadyEdit", method = POST)
    @Transactional
    public Map<String, Object> transferWaitingToReadyPartial(@RequestParam("warehouseProductId") Long warehouseProductId,
                                                             @RequestParam("reason") String reason,
                                                             @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setReason(reason);
        warehouseProduct.setAction(KOCHIRISH);
        warehouseProduct.setDocumentStatus(READY);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        List<WarehouseProductItem> items = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId);
        items.forEach(i -> warehouseProductItemService.deleteWarehouseProductItem(i.getId()));
        if (whItems != null && whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProduct);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }
        itemStatusService.countTransferWhDoc(warehouseProduct);

        WarehouseProduct warehouseProductK = new WarehouseProduct();
        warehouseProductK.setCreatedAt(new Date());
        warehouseProductK.setAction(KIRIM);
        warehouseProductK.setDocumentType(warehouseProduct.getDocumentType());
        warehouseProductK.setFromWarehouse(warehouseProduct.getFromWarehouse());
        warehouseProductK.setToWarehouse(warehouseProduct.getToWarehouse());
        warehouseProductK.setDocumentStatus(PENDING);
        warehouseProductK.setLinkedWarehouse(warehouseProduct);
        Utils.generateRegNumber(warehouseProductK, warehouseProductService);
        warehouseProductService.saveWarehouseProduct(warehouseProductK);
        if (whItems != null && whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProductK);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setRemaining(whItems.get(i + 2));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }


    @RequestMapping(path = "/receivePendingDocAll", method = POST)
    @Transactional
    public Map<String, Object> createWarehouseProductPendingReceive(@RequestParam("warehouseProductId") Long warehouseProductId,
                                                                    @RequestParam("reason") Optional<String> reason,
                                                                    @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {

        if (warehouseProductId == null) throw new RecordNotFoundException("WarehouseProduct id is not found");
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
        warehouseProduct.setAction(KIRIM);
        warehouseProduct.setDocumentStatus(READY);
        reason.ifPresent(warehouseProduct::setReason);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        if (whItems == null || whItems.size() < 1) throw new RecordNotFoundException("list is empty...");
        List<WarehouseProductItem> items = new ArrayList<>();
        for (int i = 0; i < whItems.size(); i = i + 4) {
            WarehouseProductItem whDocumentItem = warehouseProductItemService.getWarehouseProductItemById(Long.parseLong(whItems.get(i + 3).toString()));
            whDocumentItem.setWarehouseProduct(warehouseProduct);
            whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
            whDocumentItem.setIncomePrice(whItems.get(i + 1));
            whDocumentItem.setCount(whItems.get(i + 2));
            whDocumentItem.setRemaining(new BigDecimal(0));
            whDocumentItem.setCreatedAt(new Date());
            warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            items.add(whDocumentItem);
        }
        warehouseProduct.setWhItems(items);
        itemStatusService.countIncomeWhDocLocal(warehouseProduct);
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/receivePendingDocPartial", method = POST)
//    @PreAuthorize("hasAuthority('WarehouseProductCreate')")
    @Transactional
    public Map<String, Object> warehouseProductPendingReceivePartial(@RequestParam("warehouseProductId") Long productId,
                                                                     @RequestParam("reason") Optional<String> reason,
                                                                     @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {
        if (productId == null) throw new RecordNotFoundException("WarehouseProduct id is not found");
        if (whItems == null || whItems.size() < 1) throw new RecordNotFoundException("list is empty...");
        boolean full = true;
        for (int i = 0; i < whItems.size(); i = i + 4) {
            WarehouseProductItem whDocumentItemOld = warehouseProductItemService.getWarehouseProductItemById(Long.parseLong(whItems.get(i + 3).toString()));
            if (!whDocumentItemOld.getRemaining().equals(whItems.get(i + 2))) full = false;
        }
        if (full) {
            WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(productId);
            warehouseProduct.setAction(KIRIM);
            warehouseProduct.setDocumentStatus(READY);
            reason.ifPresent(warehouseProduct::setReason);
            warehouseProduct.setCreatedAt(new Date());
            warehouseProductService.saveWarehouseProduct(warehouseProduct);
            List<WarehouseProductItem> items = new ArrayList<>();
            warehouseProduct.setWhItems(items);
            for (int i = 0; i < whItems.size(); i = i + 4) {
                WarehouseProductItem whDocumentItem = warehouseProductItemService.getWarehouseProductItemById(Long.parseLong(whItems.get(i + 3).toString()));
                whDocumentItem.setWarehouseProduct(warehouseProduct);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setRemaining(new BigDecimal(0));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
                items.add(whDocumentItem);
            }
            warehouseProduct.setWhItems(items);
            itemStatusService.countIncomeWhDocLocal(warehouseProduct);
        } else {
            WarehouseProduct warehouseProductLink = warehouseProductService.getWarehouseProductById(productId);
            WarehouseProduct warehouseProduct = new WarehouseProduct();
            warehouseProduct.setLinkedWarehouse(warehouseProductLink.getLinkedWarehouse());
            warehouseProduct.setCreatedAt(new Date());
            warehouseProduct.setAction(KIRIM);
            warehouseProduct.setDocumentType(NAKLADNOY);
            warehouseProduct.setFromWarehouse(warehouseProductLink.getFromWarehouse());
            warehouseProduct.setToWarehouse(warehouseProductLink.getToWarehouse());
            warehouseProduct.setDocumentStatus(READY);
            reason.ifPresent(warehouseProduct::setReason);
            Utils.generateRegNumber(warehouseProduct, warehouseProductService);
            warehouseProductService.saveWarehouseProduct(warehouseProduct);
            List<WarehouseProductItem> items = new ArrayList<>();
            for (int i = 0; i < whItems.size(); i = i + 4) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProduct);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
                items.add(whDocumentItem);
                WarehouseProductItem whDocumentItemOld = warehouseProductItemService.getWarehouseProductItemById(Long.parseLong(whItems.get(i + 3).toString()));
                whDocumentItemOld.setRemaining(whDocumentItemOld.getRemaining().subtract(whDocumentItem.getCount()));
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItemOld);
            }
            warehouseProduct.setWhItems(items);
            itemStatusService.countIncomeWhDoc(warehouseProduct);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }


    @RequestMapping(path = "/createWhTransfer", method = POST)
    public Map<String, Object> createWarehouseProductTransfer(
            @RequestParam("requestId") Long requestId,
            @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {

        Optional<WarehouseRequest> warehouseRequest = warehouseRequestRepository.findById(requestId);
        if (!warehouseRequest.isPresent()) throw new RecordNotFoundException("");

        WarehouseProductDto warehouseProduct = new WarehouseProductDto();
        warehouseProduct.setFromWarehouseId(warehouseRequest.get().getToWarehouse().getId());
        warehouseProduct.setFromWarehouseName(warehouseRequest.get().getToWarehouse().getName());
        warehouseProduct.setToWarehouseId(warehouseRequest.get().getFromWarehouse().getId());
        warehouseProduct.setToWarehouseName(warehouseRequest.get().getFromWarehouse().getName());
        warehouseProduct.setDocumentType(NAKLADNOY);
        warehouseProduct.setDocumentStatus(WarehouseProduct.DocumentStatus.DRAFT);
        warehouseProduct.setAction(KOCHIRISH);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setUpdatedAt(new Date());
        Utils.generateRegNumber(warehouseProduct, warehouseProductService);
        warehouseProduct.setLinkedWarehouseRequestId(warehouseRequest.get().getId());
        warehouseProduct.setLinkedWarehouseRequestRegNumber(warehouseRequest.get().getRegNumber());

        List<WarehouseProductItemDto> warehouseProductItems = new ArrayList<>(whItems.size());
        if (whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                if (whItems.get(i + 1).compareTo(ZERO) > 0) {
                    WarehouseProductItemDto whProductItem = new WarehouseProductItemDto();
                    ProductDto productDto = new ProductDto(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                    whProductItem.setProductDto(productDto);
                    whProductItem.setProductId(Long.parseLong(whItems.get(i).toString()));
                    whProductItem.setIncomePrice(whItems.get(i + 1));
                    whProductItem.setCount(whItems.get(i + 2));
                    whProductItem.setRemaining(whItems.get(i + 2));
                    warehouseProductItems.add(whProductItem);
                }
            }
        }
        warehouseProduct.setWhItems(warehouseProductItems);

        Map<String, Object> map = new HashMap<>();
        map.put("product", warehouseProduct);
        map.put("items", warehouseProductItems);
        return map;
    }

    @Transactional
    @PostMapping(path = "readyWhTransfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> testGetJSON(@RequestBody Map<String, Object> obj) throws RecordNotFoundException, ParseException {
        Map<String, Object> product = (Map<String, Object>) obj.get("product");
        List<Object> whItems = (List<Object>) obj.get("whItems");
        Integer toWhId = (Integer) product.get("toWarehouseId");
        Integer fromWhId = (Integer) product.get("fromWarehouseId");
        Integer idInt = (Integer) product.get("linkedWarehouseRequestId");
        Long reqId = Long.parseLong(String.valueOf(idInt));

        Optional<WarehouseRequest> request = warehouseRequestRepository.findById(reqId);
        if (!request.isPresent()) throw new RecordNotFoundException("request not found");
        if (request.get().getFullSent()) throw new RecordNotFoundException("All Product have sent...");

        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setLinkedWarehouseRequest(request.get());
        warehouseProduct.setFromWarehouse(warehouseService.getWarehouseById(Long.parseLong(String.valueOf(fromWhId))));
        warehouseProduct.setToWarehouse(warehouseService.getWarehouseById(Long.parseLong(String.valueOf(toWhId))));
        warehouseProduct.setRegNumber((String) product.get("regNumber"));
        warehouseProduct.setReason((String) product.get("reason"));
        warehouseProduct.setDocumentStatus(READY);
        warehouseProduct.setDocumentType(NAKLADNOY);
        warehouseProduct.setAction(KOCHIRISH);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setUpdatedAt(new Date());
        warehouseProductService.saveWarehouseProduct(warehouseProduct);

        WarehouseRequest warehouseRequest = warehouseProduct.getLinkedWarehouseRequest();
        List<WarehouseRequestItem> warehouseRequestItems = warehouseRequestItemRepository.findAllByWarehouseRequestId(warehouseRequest.getId());
        List<WarehouseProductItem> warehouseProductItems = new ArrayList<>(whItems.size());
        if (whItems.size() > 0) {
            boolean full = true;
            for (Object item : whItems) {
                Map<String, Object> productItem = (Map<String, Object>) item;
                WarehouseProductItem whProductItem = new WarehouseProductItem();
                whProductItem.setWarehouseProduct(warehouseProduct);
                whProductItem.setProduct(productService.getProductById(Long.parseLong(String.valueOf(productItem.get("productId")))));
                Integer count = (Integer) productItem.get("count");
                if (count == null || count == 0) throw new RecordNotFoundException("Invalid count is...");
                Integer incomePrice = (Integer) productItem.get("incomePrice");
                if (incomePrice == null || incomePrice == 0) throw new RecordNotFoundException("Invalid count is...");
                whProductItem.setIncomePrice(new BigDecimal(incomePrice));
                whProductItem.setCount(new BigDecimal(count));
                whProductItem.setRemaining(new BigDecimal(count));
                warehouseProductItemService.saveWarehouseProductItem(whProductItem);
                warehouseProductItems.add(whProductItem);
                for (WarehouseRequestItem warehouseRequestItem : warehouseRequestItems) {
                    if (warehouseRequestItem.getProduct().getId().equals(whProductItem.getProduct().getId())) {
                        if (warehouseRequestItem.getRemaining().compareTo(whProductItem.getCount()) < 0) {
                            throw new RecordNotFoundException("Invalid count ...");
                        }
                        warehouseRequestItem.setRemaining(warehouseRequestItem.getRemaining().subtract(whProductItem.getCount()));
                        warehouseRequestItemRepository.save(warehouseRequestItem);
                        if (!warehouseRequestItem.getRemaining().equals(ZERO)) full = false;
                    }
                }
            }
            if (full) {
                warehouseRequest.setFullSent(true);
                warehouseRequest.setDocumentStatus(WarehouseRequest.DocumentStatus.READY);
                warehouseRequestRepository.save(warehouseRequest);
            }
        }
        warehouseProduct.setWhItems(warehouseProductItems);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        itemStatusService.countTransferWhDocWithRequisite(warehouseProduct);

        WarehouseProduct warehouseProductK = new WarehouseProduct();
        warehouseProductK.setCreatedAt(new Date());
        warehouseProductK.setAction(KIRIM);
        warehouseProductK.setDocumentType(warehouseProduct.getDocumentType());
        warehouseProductK.setFromWarehouse(warehouseProduct.getFromWarehouse());
        warehouseProductK.setToWarehouse(warehouseProduct.getToWarehouse());
        warehouseProductK.setDocumentStatus(PENDING);
        warehouseProductK.setLinkedWarehouse(warehouseProduct);
        warehouseProductK.setLinkedWarehouseRequest(warehouseRequest);
        Utils.generateRegNumber(warehouseProductK, warehouseProductService);
        warehouseProductService.saveWarehouseProduct(warehouseProductK);
        if (warehouseProductItems.size() > 0) {
            for (WarehouseProductItem warehouseProductItem : warehouseProductItems) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProductK);
                whDocumentItem.setProduct(warehouseProductItem.getProduct());
                whDocumentItem.setIncomePrice(warehouseProductItem.getIncomePrice());
                whDocumentItem.setCount(warehouseProductItem.getCount());
                whDocumentItem.setRemaining(warehouseProductItem.getRemaining());
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }


    @RequestMapping(path = "/warehouseProductExpenseCreateWaiting", method = POST)
    @Transactional
    public Map<String, Object> createWarehouseProductExpenseWaiting(@RequestParam("createdAt") Date createdAt,
                                                                    @RequestParam("documentType") WarehouseProduct.DocumentType documentType,
                                                                    @RequestParam("reason") String reason,
                                                                    @RequestParam("fromWarehouseId") Long fromWarehouseId,
                                                                    @RequestParam("regNumber") String regNumber,
                                                                    @RequestParam("documentStatus") WarehouseProduct.DocumentStatus documentStatus,
                                                                    @RequestParam("contragentId") Optional<Long> contragentId,
                                                                    @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {

        WarehouseProduct warehouseProduct = new WarehouseProduct();
        if (contragentId.isPresent())
            warehouseProduct.setContragent(contragentService.getContragentById(contragentId.get()));
        warehouseProduct.setCreatedAt(createdAt);
        warehouseProduct.setReason(reason);
        warehouseProduct.setAction(CHIQIM);
        warehouseProduct.setDocumentType(documentType);
        warehouseProduct.setRegNumber(regNumber);
        warehouseProduct.setFromWarehouse(warehouseService.getWarehouseById(fromWarehouseId));
        warehouseProduct.setDocumentStatus(documentStatus);
        warehouseProduct.setDocumentStatus(WAITING);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        if (whItems != null && whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProduct);
                whDocumentItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whDocumentItem.setIncomePrice(whItems.get(i + 1));
                whDocumentItem.setCount(whItems.get(i + 2));
                whDocumentItem.setRemaining(whItems.get(i + 2));
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/warehouseRequestCreateWaiting", method = POST)
    @Transactional
    public Map<String, Object> createWarehouseRequestWaiting(@RequestParam("reason") String reason, @RequestParam("toWarehouseId") Long toWarehouseId,
                                                             @RequestParam("fromWarehouseId") Long fromWarehouseId, @RequestParam("regNumber") String regNumber,
                                                             @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {
        WarehouseRequest warehouseRequest = new WarehouseRequest();
        warehouseRequest.setCreatedAt(new Date());
        warehouseRequest.setReason(reason);
        warehouseRequest.setRegNumber(regNumber);
        warehouseRequest.setRegDate(new Date());
        warehouseRequest.setToWarehouse(warehouseService.getWarehouseById(toWarehouseId));
        warehouseRequest.setFromWarehouse(warehouseService.getWarehouseById(fromWarehouseId));
        warehouseRequest.setDocumentStatus(WarehouseRequest.DocumentStatus.WAITING);
        warehouseRequestRepository.save(warehouseRequest);
        if (whItems != null && whItems.size() > 0) {
            for (int i = 0; i < whItems.size(); i = i + 3) {
                WarehouseRequestItem whRequestItem = new WarehouseRequestItem();
                whRequestItem.setWarehouseRequest(warehouseRequest);
                whRequestItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                whRequestItem.setCount(whItems.get(i + 2));
                whRequestItem.setRemaining(whItems.get(i + 2));
                warehouseRequestItemRepository.save(whRequestItem);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/get-all-warehouses-without-warehouse-id", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> getAllWarehousesWithoutWarehouseId(@RequestParam(name = "fromWarehouseId") Long fromWarehouseId) {
        List<Warehouse> warehouses = warehouseService.getAllWarehousesWithoutSelectedWarehouseId(fromWarehouseId);
        Map<String, Object> map = new HashMap<>();
        if (warehouses == null || warehouses.size() == 0) {
            map.put("warehouses", new ArrayList<>());
            map.put("status", Boolean.FALSE);
        } else {
            map.put("warehouses", warehouses);
            map.put("status", Boolean.TRUE);
        }
        return map;
    }

    @RequestMapping(path = "/get-all-warehouses-with-production-id", method = POST)
//    @PreAuthorize("hasAuthority('Check Password')")
    public Map<String, Object> getAllWarehousesWithoutProductionId(@RequestParam(name = "productionId") Long productionId) {
        List<Warehouse> warehouses = warehouseService.getAllCompanyWarehousesWithProductionId(productionId);
        Map<String, Object> map = new HashMap<>();
        if (warehouses == null || warehouses.size() == 0) {
            map.put("warehouses", new ArrayList<>());
            map.put("status", Boolean.FALSE);
        } else {
            map.put("warehouses", warehouses);
            map.put("status", Boolean.TRUE);
        }
        return map;
    }

    @RequestMapping(path = "/productCreate", method = POST)
    @Transactional
    public Map<String, Object> createProduct(
            @RequestParam(name = "productCategory") Long productCategoryId,
            @RequestParam(name = "productType") Long productTypeId,
            @RequestParam(name = "productUnit") Long productUnitId,
            @RequestParam(name = "nameUz") String nameUz,
            @RequestParam(name = "nameRu") String nameRu,
            @RequestParam(name = "nameEn") String nameEn,
            @RequestParam(name = "description") String description,
//                                    @RequestParam(name = "productPhotoUpload") MultipartFile productPhoto,
            @RequestParam(name = "isWrapped") Optional<Boolean> isWrapped) throws RecordNotFoundException, IOException {


        Product product = new Product();
        product.setProductCategory(productCategoryService.getProductCategoryById(productCategoryId));
        product.setProductType(productTypeService.getProductTypeById(productTypeId));
        product.setProductUnit(productUnitService.getProductUnitById(productUnitId));
        product.setNameRu(nameRu);
        isWrapped.ifPresent(product::setIsWrapped);
        product.setNameUz(nameUz);
        product.setNameEn(nameEn);
        product.setDescription(description);
        product.setCreatedAt(new Date());
//        productService.forSaveProductPhoto(product, productPhoto);
        productService.saveProduct(product);

        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }


    @RequestMapping(path = "/contragentCreate", method = POST)
    @Transactional
    public Map<String, Object> createContragent(
            @RequestParam(name = "regionId") Long regionId,
            @RequestParam(name = "fullName") String fullName,
            @RequestParam(name = "shortName") String shortName,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "mobile") String mobile,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "address2") String address2,
            @RequestParam(name = "inn") String inn,
            @RequestParam(name = "okonx") String okonx,
            @RequestParam(name = "bankAccount") String bankAccount,
            @RequestParam(name = "vatAccount") String vatAccount) throws RecordNotFoundException {

        Contragent contragent = new Contragent();
        if (regionId != null) contragent.setRegion(regionService.getRegionById(regionId));
        contragent.setFullName(fullName);
        contragent.setShortName(shortName);
        contragent.setPhone(phone);
        contragent.setMobile(mobile);
        contragent.setEmail(email);
        contragent.setAddress(address);
        contragent.setAddress2(address2);
        contragent.setInn(inn);
        contragent.setOkonx(okonx);
        contragent.setBankAccount(bankAccount);
        contragent.setVatAccount(vatAccount);
        contragentService.saveContragent(contragent);

        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/createConversion", method = POST)
    @Transactional
    public Map<String, Object> createConversion(
            @RequestParam("productionId") Long productionId,
            @RequestParam("productId") Long productId,
            @RequestParam("price") BigDecimal price,
            @RequestParam("count") BigDecimal count,
            @RequestParam("whItems") List<BigDecimal> whItems) throws RecordNotFoundException {
        Warehouse production = warehouseService.getWarehouseById(productionId);
        if (production == null) throw new RecordNotFoundException("production not found ");
        Product product = productService.getProductById(productId);
        if (product == null) throw new RecordNotFoundException("product not found ");
        Conversion conversion = new Conversion();
        conversion.setProduction(production);
        conversion.setDate(new Date());
        conversion.setProduct(product);
        conversion.setCount(count);
        conversion.setPrice(price);
        conversion.setAmount(count.multiply(price));
        conversionService.saveConversion(conversion);
        if (whItems.isEmpty()) throw new RecordNotFoundException("product not found ");
        for (int i = 0; i < whItems.size(); i = i + 3) {
            if (whItems.get(i + 1).compareTo(ZERO) > 0) {
                ConversionItem conversionItem = new ConversionItem();
                conversionItem.setConversion(conversion);
                conversionItem.setProduct(productService.getProductById(Long.parseLong(whItems.get(i).toString())));
                conversionItem.setPrice(whItems.get(i + 1));
                conversionItem.setCount(whItems.get(i + 2));
                conversionItem.setAmount(whItems.get(i + 1).multiply(whItems.get(i + 2)));
                conversionItemService.saveConversionItem(conversionItem);
                itemStatusService.subtractConversionItem(conversionItem);
            }
        }
        itemStatusService.addConversion(conversion);
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/monthlyConversionReport", method = POST)
    @Transactional
    public Map<String, Object> monthlyConversionReport(
            @RequestParam("productionId") Optional<Long> productionId,
            @RequestParam("productId") Optional<Long> productId,
            @RequestParam("searchDate") Optional<String> searchDate) throws RecordNotFoundException, ParseException {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
        Map<String, Object> map = new HashMap<>();
        if (searchDate.isPresent() && !searchDate.get().equals("") && productionId.isPresent() && productId.isPresent()) {
            LocalDate localDate = sm.parse(searchDate.get()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            List<Object[]> conversion = conversionService.allByProductionIdByProductIdBySearchDate(productionId.get(), productId.get(), localDate.getMonthValue(), localDate.getYear());
            List<Object[]> conversionItems = conversionItemService.allByProductionIdByProductIdBySearchDate(productionId.get(), localDate.getMonthValue(), localDate.getYear());

            map.put("productCount", conversion.get(0)[0]);
            map.put("productPrice", conversion.get(0)[1]);
            map.put("productAmount", conversion.get(0)[2]);
            map.put("items", conversionItems);
            map.put("status", this.getStatusSuccess());
            map.put("message", conversionItems.isEmpty() ? "Ma'lumot mavjud emas!" : "Ma'lumot muvoffaqiyatli yuklandi!!!");
            map.put("have", !conversionItems.isEmpty());
        } else {
            map.put("status", this.getStatusError());
            map.put("message", "Ma'lumot mavjud emas!");
            map.put("have", false);
        }

        return map;
    }

    @RequestMapping(path = "/monthlyConversionCreate", method = POST)
    @Transactional
    public Map<String, Object> createMonthlyConversion(
            @RequestParam(name = "productionId") Long productionId,
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "searchDate") String date,
            @RequestParam(name = "count") BigDecimal count,
            @RequestParam(name = "price") BigDecimal price,
            @RequestParam(name = "amount") BigDecimal amount,
            @RequestParam(name = "conversionItems") List<BigDecimal> monthlyConversionItems,
            @RequestParam(name = "conversionExpenses") List<BigDecimal> conversionExpenses) throws RecordNotFoundException, ParseException {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
        MonthlyConversion monthlyConversion = new MonthlyConversion();
        monthlyConversion.setDate(sm.parse(date));
        monthlyConversion.setCount(count);
        monthlyConversion.setPrice(price);
        monthlyConversion.setAmount(amount);
        if (productionId != null)
            monthlyConversion.setProduction(warehouseService.getWarehouseById(productionId));
        if (productId != 0) monthlyConversion.setProduct(productService.getProductById(productId));
        monthlyConversionService.saveMonthlyConversion(monthlyConversion);
        for (int i = 0; i < monthlyConversionItems.size(); i = i + 4) {
            MonthlyConversionItem conversionItems = new MonthlyConversionItem();
            conversionItems.setMonthlyConversion(monthlyConversion);
            conversionItems.setProduct(productService.getProductById(Long.parseLong(monthlyConversionItems.get(i).toString())));
            conversionItems.setPrice(monthlyConversionItems.get(i + 1));
            conversionItems.setCount(monthlyConversionItems.get(i + 2));
            conversionItems.setAmount(monthlyConversionItems.get(i + 3));
            monthlyConversionItemRepository.save(conversionItems);

        }
        for (int i = 0; i < conversionExpenses.size(); i = i + 4) {
            ProductionExpense productionExpense = new ProductionExpense();
            productionExpense.setMonthlyConversion(monthlyConversion);
            productionExpense.setExpenseType(expenseTypeService.getExpenseTypeById(Long.parseLong(conversionExpenses.get(i).toString())));
            productionExpense.setAmount(conversionExpenses.get(i + 1));
            productionExpense.setPrice(conversionExpenses.get(i + 2));
            productionExpense.setOverAll(conversionExpenses.get(i + 3));
            productionExpenseRepository.save(productionExpense);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status", this.getStatusSuccess());
        map.put("message", "Ma'lumot muvoffaqiyatli yuklandi!!!");
        return map;
    }

    @RequestMapping(path = "/warehouseWorkingReport", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> warehouseWorkingReport(
            @RequestParam("warehouseId") Optional<Long> warehouseId,
            @RequestParam("action") String action,
            @RequestParam("destination") String destination,
            @RequestParam("searchDate") Optional<String> searchDate,
            @RequestParam("searchDate2") Optional<String> searchDate2) throws RecordNotFoundException, ParseException {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> map = new HashMap<>();
        if (searchDate.isPresent() && !searchDate.get().equals("") && warehouseId.isPresent()) {
            LocalDate localDate = sm.parse(searchDate.get()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            List<Object[]> warehouseProducts = new ArrayList<>();
            if(destination.equalsIgnoreCase("DAY")){
                if(KIRIM.toString().equalsIgnoreCase(action)) {
                    warehouseProducts = warehouseProductService.getAllWarehouseForWorkingReportByToWarehouse(KIRIM, warehouseId.get(), localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
                }else if(CHIQIM.toString().equalsIgnoreCase(action)){
                    warehouseProducts = warehouseProductService.getAllWarehouseForWorkingReportByFromWarehouse(CHIQIM, warehouseId.get(), localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
                }else {
                    warehouseProducts = warehouseProductService.getAllWarehouseForWorkingReportByToWarehouse(KIRIM, warehouseId.get(), localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
                    warehouseProducts.addAll(warehouseProductService.getAllWarehouseForWorkingReportByFromWarehouse(CHIQIM, warehouseId.get(), localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear()));
                }
            }else {
                if (searchDate2.isPresent() && !searchDate2.get().equals("")) {
                    Date date = sm.parse(searchDate.get());
                    Date date2 = sm.parse(searchDate2.get());
                    if(KIRIM.toString().equalsIgnoreCase(action)) {
                        warehouseProducts = warehouseProductService.getAllWarehouseForWorkingReportByToWarehouseByDate(KIRIM, warehouseId.get(), date, date2);
                    }else if(CHIQIM.toString().equalsIgnoreCase(action)){
                        warehouseProducts = warehouseProductService.getAllWarehouseForWorkingReportByFromWarehouseByDate(CHIQIM, warehouseId.get(), date, date2);
                    }else {

                        warehouseProducts = warehouseProductService.getAllWarehouseForWorkingReportByToWarehouseByDate(KIRIM, warehouseId.get(), date, date2);
                        warehouseProducts.addAll(warehouseProductService.getAllWarehouseForWorkingReportByFromWarehouseByDate(CHIQIM, warehouseId.get(), date, date2));
                    }
                }else {
                    map.put("status", this.getStatusError());
                    map.put("message", "Ma'lumot mavjud emas!");
                    map.put("have", false);
                }
            }

            map.put("items", warehouseProducts);
            map.put("status", this.getStatusSuccess());
            map.put("message", warehouseProducts.isEmpty() ? "Ma'lumot mavjud emas!" : "Ma'lumot muvoffaqiyatli yuklandi!!!");
            map.put("have", !warehouseProducts.isEmpty());
        } else {
            map.put("status", this.getStatusError());
            map.put("message", "Ma'lumot mavjud emas!");
            map.put("have", false);
        }
        return map;
    }

    @RequestMapping(path = "/warehouseWorkingReportItems", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> warehouseWorkingReportItems(@RequestParam("warehouseProductId") Optional<Long> warehouseProductId) throws RecordNotFoundException, ParseException {
        Map<String, Object> map = new HashMap<>();
        if (warehouseProductId.isPresent()) {
            List<Object[]> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductIdForWorkingReport(warehouseProductId.get());
            map.put("items", warehouseProductItems);
            map.put("status", this.getStatusSuccess());
            map.put("message", warehouseProductItems.isEmpty() ? "Ma'lumot mavjud emas!" : "Ma'lumot muvoffaqiyatli yuklandi!!!");
            map.put("have", !warehouseProductItems.isEmpty());
        } else {
            map.put("status", this.getStatusError());
            map.put("message", "Ma'lumot mavjud emas!");
            map.put("have", false);
        }
        return map;
    }

    @RequestMapping(path = "/uploadByExcel", method = POST)
    public Map<String, List<Map<String, Object>>> uploadByExcel(@RequestParam(name = "uploadExcel", required = false) MultipartFile upload) throws Throwable {
        if (upload == null) throw new RecordNotFoundException("File is empty");
        File file = multipartToFile(upload, upload.getOriginalFilename());
        List<Map<Object, Object>> wholeFile = Utils.parseExcelFile(file);
        if (wholeFile.isEmpty()) throw new RecordNotFoundException("Upload file is empty");
        List<Map<String, Object>> listPros = new ArrayList<>(wholeFile.size());
        List<Map<String, Object>> listCons = new ArrayList<>(wholeFile.size());
        for (Map<Object, Object> oneRowOfFile : wholeFile) {
            Map<String, Object> mapPro = new HashMap<>(3);
            if (oneRowOfFile.get("Код") == null || oneRowOfFile.get("Код").toString().equals(""))
                throw new RecordNotFoundException("Код is empty");
            if (oneRowOfFile.get("Входящая цена") != null && !oneRowOfFile.get("Входящая цена").toString().equals(""))
                mapPro.put("income", new BigDecimal(oneRowOfFile.get("Входящая цена").toString().trim()));
            else mapPro.put("income", ZERO);
            if (oneRowOfFile.get("Количество") != null && !oneRowOfFile.get("Количество").toString().equals(""))
                mapPro.put("count", new BigDecimal(oneRowOfFile.get("Количество").toString().trim()));
            else mapPro.put("count", ZERO);
            String code = oneRowOfFile.get("Код").toString().trim();
            Product product = productService.findFirstByCode(code);
            if (product == null) {
                mapPro.put("productId", null);
                mapPro.put("productNameUz", "no data");
                mapPro.put("productNameRu", "no data");
                mapPro.put("productNameEn", "no data");
                mapPro.put("productCode", code);
                mapPro.put("productUnitId", null);
                mapPro.put("productUnitNameUz", "no data");
                mapPro.put("productUnitNameRu", "no data");
                mapPro.put("productUnitNameEn", "no data");
                mapPro.put("multiplication", "no data");
                listCons.add(mapPro);
            } else {
                mapPro.put("productId", product.getId());
                mapPro.put("productNameUz", product.getNameUz());
                mapPro.put("productNameRu", product.getNameRu());
                mapPro.put("productNameEn", product.getNameEn());
                mapPro.put("productCode", product.getCode());
                if (product.getProductUnit() != null) {
                    mapPro.put("productUnitId", product.getProductUnit().getId());
                    mapPro.put("productUnitNameUz", product.getProductUnit().getNameUz());
                    mapPro.put("productUnitNameRu", product.getProductUnit().getNameRu());
                    mapPro.put("productUnitNameEn", product.getProductUnit().getNameEn());
                }
                mapPro.put("multiplication", (new BigDecimal(mapPro.get("count").toString())).multiply(new BigDecimal(mapPro.get("income").toString())));
                listPros.add(mapPro);
            }
        }
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        map.put("pros", listPros);
        map.put("cons", listCons);
        return map;
    }

    public File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    @RequestMapping(path = "/getReportByWarehouse", method = POST)
    public Map<String, List<ReportOneDto>> getReportByWarehouse(@RequestParam(name = "warehouseId") Long warehouseId) {
        List<ReportOneDto> response = itemBalanceService.getAllProductByWarehouseId(warehouseId);
        Map<String, List<ReportOneDto>> mapResponse = new LinkedHashMap<>();
        mapResponse.put("data", response);
        return mapResponse;
    }

    @RequestMapping(path = "/getWarehouseWorkingReportByWarehouse", method = POST)
    public Map<String, List<ReportOneDto>> getWarehouseWorkingReportByWarehouse(@RequestParam(name = "warehouseId") Long warehouseId) {
        List<ReportOneDto> response = itemBalanceService.getAllProductByWarehouseId(warehouseId);
        Map<String, List<ReportOneDto>> mapResponse = new LinkedHashMap<>();
        mapResponse.put("data", response);
        return mapResponse;
    }
}
