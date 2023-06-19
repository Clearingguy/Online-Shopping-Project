import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.io.FileWriter;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class ProductDetails extends JFrame {
 private JButton backButton;
    private JButton addToCartButton;
    private JLabel quantityLabel;
    private int initialQuantity;

    public ProductDetails(ProductCatalog productCatalog, String productName, String category, String ram, double price,
            int quantity) {
        setTitle(productName + " Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        initialQuantity = quantity; 

        JLabel nameLabel = new JLabel("Product Name: " + productName);
        JLabel categoryLabel = new JLabel("Category: " + category);
        JLabel ramLabel = new JLabel("RAM: " + ram);
        JLabel priceLabel = new JLabel("Price: " + formatPrice(price));
        quantityLabel = new JLabel("Quantity: " + quantity);

        JPanel detailsPanel = new JPanel(new GridLayout(6, 1));
        detailsPanel.add(nameLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(ramLabel);
        detailsPanel.add(priceLabel);
        detailsPanel.add(quantityLabel);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(ProductDetails.this, "Enter Quantity:");
                if (input != null) {
                    try {
                        int selectedQuantity = Integer.parseInt(input);
                        int cartQuantity = getCartQuantity(selectedQuantity);
                        if (cartQuantity <= quantity) {
                            productCatalog.addToCart(
                                    new Product(productName, category, ram, price, selectedQuantity));
                            updateProductQuantity(productName, category, ram, price, quantity - selectedQuantity);
                            JOptionPane.showMessageDialog(ProductDetails.this, "Product added to cart.",
                                    "Add to Cart", JOptionPane.INFORMATION_MESSAGE);
                            updateQuantityLabel(quantity - selectedQuantity);
                        } else {
                            JOptionPane.showMessageDialog(ProductDetails.this, "Not enough quantity available.",
                                    "Add to Cart", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ProductDetails.this, "Invalid quantity.", "Add to Cart",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productCatalog.showProductCatalog();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addToCartButton);
        buttonPanel.add(backButton);

        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showBackButton(boolean show) {
        backButton.setVisible(show);
    }

    private String formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("PHP #,##0.00");
        return decimalFormat.format(price);
    }

    private int getCartQuantity(int selectedQuantity) {
        int currentCartQuantity = 0;
        if (selectedQuantity > 0) {
            currentCartQuantity = selectedQuantity;
        }
        return currentCartQuantity;
    }

    private void updateProductQuantity(String productName, String category, String ram, double price,
            int updatedQuantity) {
        String folderPath = "C:/Users/Gabion/Documents/Online Shopping/Product";
        String filePath = folderPath + "/" + productName + ".txt";

        File productFile = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productFile))) {
            writer.write(productName);
            writer.newLine();
            writer.write(category);
            writer.newLine();
            writer.write(ram);
            writer.newLine();
            writer.write(String.valueOf(price));
            writer.newLine();
            writer.write(String.valueOf(initialQuantity)); 
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateQuantityLabel(updatedQuantity);
    }

    public void updateQuantityLabel(int quantity) {
        quantityLabel.setText("Quantity: " + quantity);
    }
}

