package vertexid.nhmarket.pcs.cmmn;

/**
 * 객체화 과정에서 존재하지 않는 필드일 경우 즉 맵이 POJO 오브젝트의 정보가 아닌 다른 정보도 갖고 있는 경우 이 Excetpion션이 발생할 수
 * 있음
 * 
 * @since 2016-01-28
 * @author Gracefulife
 */
public class InvalidFieldExeption extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public InvalidFieldExeption() {
		super("오브젝트에 존재하지 않는 필드입니다.");
	}

	public void printStackTrace(String key) {
		super.printStackTrace();
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}