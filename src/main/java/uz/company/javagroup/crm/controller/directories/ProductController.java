package uz.isd.javagroup.grandcrm.controller.directories;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.DivTagWorker;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.node.IElementNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;
import uz.isd.javagroup.grandcrm.entity.directories.ProductType;
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductCategoryService;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.directories.ProductTypeService;
import uz.isd.javagroup.grandcrm.services.directories.ProductUnitService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("directory/products")
public class ProductController extends BaseController {

    @Autowired
    ProductService productService;
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    ProductUnitService productUnitService;
    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public ProductController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @RequestMapping(path = {"/", "/{categoryId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ProductRead')")
    public ModelAndView index(@PathVariable(value = "categoryId") Optional<Long> categoryId, @ModelAttribute("status") String status,
                              @ModelAttribute("message") String messageData,  ModelAndView modelAndView) {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }
        List<Product> products;
        if (categoryId.isPresent()) products = productService.findByCategory(categoryId.get());
        else products = productService.findAll();
        List<ProductCategory> productCategories = productCategoryService.findAll();
        List<ProductType> productTypes = productTypeService.findAll();
        List<ProductUnit> productUnits = productUnitService.findAll();
        modelAndView.addObject("products", products);
        modelAndView.addObject("productCategories", productCategories);
        modelAndView.addObject("productTypes", productTypes);
        modelAndView.addObject("productUnits", productUnits);

        modelAndView.setViewName("/pages/directories/products/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductCreate')")
    public RedirectView createProduct(@RequestParam(name = "productCategory") Long productCategoryId,
                                      @RequestParam(name = "productType") Long productTypeId,
                                      @RequestParam(name = "productUnit") Long productUnitId,
                                      @RequestParam(name = "nameUz") String nameUz,
                                      @RequestParam(name = "nameRu") String nameRu,
                                      @RequestParam(name = "nameEn") String nameEn,
                                      @RequestParam(name = "description") String description,
                                      @RequestParam(name = "productPhotoUpload") MultipartFile productPhoto,
                                      @RequestParam(name = "isWrapped") Optional<Boolean> isWrapped,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {
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
        productService.forSaveProductPhoto(product, productPhoto);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/products/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductUpdate')")
    public RedirectView updateProduct(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "productCategoryId") Long productCategoryId,
                                      @RequestParam(name = "productTypeId") Long productTypeId,
                                      @RequestParam(name = "productUnitId") Long productUnitId,
                                      @RequestParam(name = "nameUzEdit") String nameUz,
                                      @RequestParam(name = "nameRuEdit") String nameRu,
                                      @RequestParam(name = "nameEnEdit") String nameEn,
                                      @RequestParam(name = "descriptionEdit") String description,
                                      @RequestParam(name = "productPhotoUploadEdit") MultipartFile productPhoto,
                                      @RequestParam(name = "isWrappedEdit") Optional<Boolean> isWrapped,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {
        Product product = productService.getProductById(id);
        product.setId(id);
        product.setProductCategory(productCategoryService.getProductCategoryById(productCategoryId));
        product.setProductType(productTypeService.getProductTypeById(productTypeId));
        product.setProductUnit(productUnitService.getProductUnitById(productUnitId));
        product.setNameRu(nameRu);
        isWrapped.ifPresent(product::setIsWrapped);
        product.setNameUz(nameUz);
        product.setNameEn(nameEn);
        product.setDescription(description);
        product.setUpdatedAt(new Date());
        productService.forUpdateProductPhoto(product, productPhoto);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/products/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductDelete')")
    public RedirectView deleteProduct(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/products/");
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('Search')")
    public RedirectView searchProduct(@RequestParam(name = "nameUzSearch") Optional<String> nameUz, RedirectAttributes redirectAttributes) {
        Product product = new Product();
        product.setNameUz(nameUz.get());
        List<Product> products = productService.findAll(product);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
//        redirectAttributes.addFlashAttribute("products", products);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        return new RedirectView("/directory/products/");
    }


    @RequestMapping(path = "/pdf")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* Do Business Logic*/
        List<Product> products = productService.findAll();

        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("products", products);
        String orderHtml = templateEngine.process("/pages/directories/products/index", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        // pdf conversion
        FontProvider dfp = new DefaultFontProvider(true, true, false);

        /* Temporary fix for display issue with blocks (in current html2pdf version (2.0.1) */
        converterProperties.setTagWorkerFactory(new LabelBlockTagWorkerFactory());
        converterProperties.setCssApplierFactory(new LabelBlockCssApplierFactory());
        converterProperties.setFontProvider(dfp);


        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    /* Temporary fix for display issue with blocks (in current html2pdf version (2.0.1) */
    private static class LabelBlockTagWorkerFactory extends DefaultTagWorkerFactory {
        @Override
        public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
            if (!TagConstants.LABEL.equals(tag.name())) {
                return null;
            }
            String display;
            if (tag.getStyles() == null || (display = tag.getStyles().get(CssConstants.DISPLAY)) == null) {
                return null;
            }
            if (CssConstants.BLOCK.equals(display)) {
                return new DivTagWorker(tag, context);
            }
            return null;
        }
    }

    /* Temporary fix for display issue with blocks (in current html2pdf version (2.0.1) */
    private static class LabelBlockCssApplierFactory extends DefaultCssApplierFactory {
        @Override
        public ICssApplier getCustomCssApplier(IElementNode tag) {
            if (!TagConstants.LABEL.equals(tag.name())) {
                return null;
            }
            String display;
            if (tag.getStyles() == null || (display = tag.getStyles().get(CssConstants.DISPLAY)) == null) {
                return null;
            }
            if (CssConstants.BLOCK.equals(display)) {
                return new BlockCssApplier();
            }
            return null;
        }
    }
}
