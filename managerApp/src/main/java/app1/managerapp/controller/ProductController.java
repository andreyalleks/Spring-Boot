package app1.managerapp.controller;

import app1.managerapp.controller.payload.UpdateProductPayLoad;
import app1.managerapp.entity.Product;
import app1.managerapp.servise.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductService productService;


    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId).orElseThrow();
    }

    @GetMapping
    public String getProduct() {

        return "catalogue/products/product";
    }


    @GetMapping("edit")
    public String getProductEditPage() {

        return "catalogue/products/edit";
    }
    @PostMapping("edit")
    public String updateProductPayload( @ModelAttribute("product")Product product, UpdateProductPayLoad payLoad){
        this.productService.updateProduct(product.getId(),payLoad.title(),payLoad.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());

    }

    @PostMapping("delete")
    public String deleteProduct( @ModelAttribute("product")Product product){
        this.productService.deleteProduct(product.getId());
        return "redirect:/catalogue/products/list";
    }

   @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response){
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",exception.getMessage());
        return "errors/404";
   }

}
