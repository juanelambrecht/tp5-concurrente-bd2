package ar.unrn.tp.jpa.servicios;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.modelo.Carrito;
import ar.unrn.tp.modelo.Clientes;
import ar.unrn.tp.modelo.NextNumber;
import ar.unrn.tp.modelo.OrdenDePago;
import ar.unrn.tp.modelo.Productos;
import ar.unrn.tp.modelo.Promociones;
import ar.unrn.tp.modelo.Tarjetas;
import ar.unrn.tp.modelo.Ventas;

public class VentaJPA implements VentaService {

	@Override
	public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-mysql");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Clientes cliente = em.find(Clientes.class, idCliente);
			Tarjetas tarjeta = em.find(Tarjetas.class, idTarjeta);

			TypedQuery<Productos> productosQuery = em.createQuery("select p from Productos p where p.id in :id",
					Productos.class);
			productosQuery.setParameter("id", productos);
			List<Productos> productosCompra = productosQuery.getResultList();

			TypedQuery<Promociones> promociones = em.createQuery(
					"select p from Promociones p where " + "?1 between p.fechaInicio and p.fechaFin",
					Promociones.class);
			promociones.setParameter(1, new Date(), TemporalType.DATE);
			List<Promociones> listaPromociones = promociones.getResultList();

			// Numero unico irrepetible --> Manejo de concurrencia
			int anioActual = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
			System.out.println(anioActual);
			TypedQuery<NextNumber> query = em.createQuery("from NextNumber where anio = :anioActual", NextNumber.class);
			query.setParameter("anioActual", anioActual);
			query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

			// Si no existe creo un registro con el anio actual mas 1
			NextNumber number = null;
			try {
				number = query.getSingleResult();
				number.setearSiguiente();
			} catch (NoResultException e) {
				number = new NextNumber(anioActual, 1);
				em.persist(number);
			}

			Carrito carrito = new Carrito((ArrayList<Promociones>) listaPromociones);
			carrito.agregarListaProductos((ArrayList<Productos>) productosCompra);
			carrito.agregarTarjeta(tarjeta);

			Ventas venta = carrito.realizarCompra(cliente);
			venta.setUniqueNumber(number.uniqueNumber());
			em.persist(venta);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

	@Override
	public float calcularMonto(List<Long> productos, Long idTarjeta) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-mysql");
		EntityManager em = emf.createEntityManager();
		// EntityTransaction tx = em.getTransaction();
		try {
			TypedQuery<Promociones> promociones = em.createQuery(
					"select p from Promociones p where " + "?1 between p.fechaInicio and p.fechaFin",
					Promociones.class);
			promociones.setParameter(1, new Date(), TemporalType.DATE);
			List<Promociones> listaPromociones = promociones.getResultList();

			TypedQuery<Productos> productosQuery = em.createQuery("select p from Productos p where p.id in :id",
					Productos.class);
			productosQuery.setParameter("id", productos);
			List<Productos> listaProductos = productosQuery.getResultList();

			Carrito carrito = new Carrito((ArrayList<Promociones>) listaPromociones);
			carrito.agregarListaProductos((ArrayList<Productos>) listaProductos);

			Tarjetas tarjeta = em.find(Tarjetas.class, idTarjeta);
			carrito.agregarTarjeta(tarjeta);
			return (float) carrito.calculoPrecioTotal();

		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

	}

	@Override
	public List<Ventas> ventas() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-mysql");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Ventas> ventas = em.createQuery("select v from Ventas v", Ventas.class);
			return ventas.getResultList();
		} catch (Exception e) {
			// tx.rollback();
			throw new RuntimeException(e);
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

}
