package ar.unrn.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.jpa.servicios.ClienteJPA;
import ar.unrn.tp.jpa.servicios.DescuentoJPA;
import ar.unrn.tp.jpa.servicios.ProductoJPA;
import ar.unrn.tp.jpa.servicios.VentaJPA;
import ar.unrn.tp.modelo.Promociones;
import ar.unrn.tp.modelo.Tarjetas;
import ar.unrn.tp.web.WebAPI;

public class Main {

	public static void main(String[] args) {
		// ClienteService client = new ClienteJPA();
//		client.crearCliente("Juan", "Smith", "30439521", "fulanito@gmail.com");
//		client.agregarTarjeta(1L, "012345678910112", "BLACK MEME", "MEMECARD", 200000);
//		// client.modificarCliente(1004L, "Juan Pablo", "Fulanito", "31439521",
//		// "fulanito@gmail.com");
//		// client.listarTarjetas(1004L);
//
//		ProductoService prod = new ProductoJPA();
//		prod.crearCategoriaProducto("Zapatillas", "Categoria Zapatillas");
//		prod.crearMarcaProducto("Zapatillas", "Marca Vans");
//		prod.crearProducto(1, "Remera nike", 1000, 3L, 4L);
//
//		DescuentoService descuento = new DescuentoJPA();
//		Date fechaCompraIni = new Date();
//		Date fechaCompraFin = new Date();
//		Date fechaProductoIni = new Date();
//		Date fechaProductoFin = new Date();
//
//		Calendar FCompra = Calendar.getInstance();
//		FCompra.setTime(fechaCompraIni);
//		FCompra.add(Calendar.DAY_OF_YEAR, -10);
//		fechaCompraIni = FCompra.getTime();
//
//		Calendar CompraFin = Calendar.getInstance();
//		CompraFin.setTime(fechaCompraFin);
//		CompraFin.add(Calendar.DAY_OF_YEAR, 3);
//		fechaCompraFin = CompraFin.getTime();
//
//		Calendar FProducto = Calendar.getInstance();
//		FProducto.setTime(fechaProductoIni);
//		FProducto.add(Calendar.DAY_OF_YEAR, -3);
//		fechaProductoIni = FProducto.getTime();
//		
//		Tarjetas tarjetaPromo = new Tarjetas("", "BLACK MEME", "MEMECARD", 100000);
//		descuento.crearDescuento(fechaProductoIni, fechaProductoFin, "Vans");
//		descuento.crearDescuentoSobreTotal(fechaProductoIni, fechaProductoFin, tarjetaPromo.getBanco());

		VentaService ventaCarrito = new VentaJPA();
		List<Long> productos = new ArrayList<Long>();
		productos.add(13L);
		ventaCarrito.realizarVenta(1L, productos, 10L);
		// System.out.println(ventaCarrito.calcularMonto(productos, 1L));
		// ventaCarrito.ventas().stream().forEach((p)->{System.out.println(p.toString());});

		// WebAPI servicio = new WebAPI(client, descuento, prod, ventaCarrito, 1234);
		// servicio.start();
	}

}
