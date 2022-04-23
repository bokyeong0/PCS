package vertexid.nhmarket.pcs.cmmn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * EgovMap 유틸리티
 * 
 * @since 2016-01-28
 * @author Gracefulife
 */
public class EgovMapUtils {
	/* Cache Object */
	private static final NotHaveStacktraceExeption EXCEPTION_ETC = new NotHaveStacktraceExeption();
	private static final InvalidFieldExeption EXCEPTION_INVALID_FIELED = new InvalidFieldExeption();
	private static final ConvertFailedException EXCEPTION_CONVERT_FAILED = new ConvertFailedException();
	
	/* Constants */
	private static final String SETTER_METHOD_PREFIX = "set";

	/**
	 * EgovMap에서 POJO 로 변환시키는 메소드
	 * 
	 * @param map
	 * @param classType
	 *            변환하고 싶은 객체의 .class 정보
	 * @return 변환된 객체
	 * @throws ConvertFailedException
	 *             변환실패
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toPojo(EgovMap map, Class<T> classType)
			throws ConvertFailedException {
		try {
			/*
			 * static generic 중첩이라 따로 타입을 생성하면 되긴 하는데 오히려 복잡해질것 같아, 그대로 사용
			 */
			return (T) convertMapToPojo(map, classType);
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw EXCEPTION_CONVERT_FAILED;
		} catch (Exception e) {
			e.printStackTrace();
			throw EXCEPTION_ETC;
		}
	}

	/**
	 * map, key, classType을 주면 캐스팅을 해서 돌려줌 ClassCastException을 던질 수 있으니, 필요한 경우
	 * 캐치할 것
	 * 
	 * @param map
	 * @param key
	 * @param classType
	 * @return
	 */
	public static <T> T getItem(EgovMap map, String key, Class<T> classType) {
		try {
			return extractValue(map.get(key), classType);
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static <T> T extractValue(Object object, Class<T> classType)
			throws ClassCastException {
		try {
			return classType.cast(object);
		} catch (ClassCastException e) {
			throw e;
		}
	}

	/*
	 * EgovMap은 Object 로 이미 생성을 해서 제네릭을 사용할 수 없기 때문에, Map으로 받는다.
	 */
	private static <T, K, V> T convertMapToPojo(Map<K, V> map,
			Class<T> classType) throws ConvertFailedException {
		try {
			T pojo = classType.newInstance();
			for (Entry<K, V> entry : map.entrySet()) {
				K key = entry.getKey();
				try {
					String setterName = generateSetterMethodName(key);
					callingSetterMethodByReflect(pojo, setterName,
							entry.getValue());
				} catch (NoSuchMethodException e) {
					EXCEPTION_INVALID_FIELED.printStackTrace((String) key);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return pojo;
		} catch (Exception e) {
			throw EXCEPTION_CONVERT_FAILED;
		}
	}

	/*
	 * Camelcase 의 키를 set키 형식으로 이름을 변경한다. 즉 userId 라는 변수가 있다면 setUserId 라는 이름을
	 * 반환한다.
	 */
	private static <K> String generateSetterMethodName(K key) {
		StringBuilder builder = new StringBuilder();
		char[] setterNames = ((String) key).toCharArray();
		setterNames[0] = Character.toUpperCase(setterNames[0]);
		return builder.append(SETTER_METHOD_PREFIX).append(setterNames)
				.toString();
	}

	/**
	 * 리플렉션을 이용하여, 주어진 객체의 setterName 함수를 밸류값으로 호출하여 사용한다.
	 * 
	 * @param pojo
	 * @param setterName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static <T, V> void callingSetterMethodByReflect(T pojo,
			String setterName, V value) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if(value == null) return;
//		Method method = pojo.getClass().getMethod(setterName, value.getClass());
		/* 서치 VO 상속으로 인한 성능저하를 막기 위해 상속한 
		 * 멤버를 포함해서 검색하지 않는 메소드로 수정 */
		Method method = pojo.getClass().getDeclaredMethod(setterName, value.getClass());
		method.invoke(pojo, value);
	}
}