import random

length = 13
result = set()

keys = ["l", "I", "1"]

for o in range(1, 100):
    # 长度 7- 13 位
    for length in range(15, 20):
        # 按照长度随机拼接
        temp = keys[random.randint(0, 1)]
        for i in range(1, length + 1):
            temp += random.choice(keys)
        result.add(temp)

print("成功生成字典，数量：", len(result))

with open("./dictionary_rules.txt", mode='w+', encoding='utf-8') as f:
    f.writelines("\n".join(result))
    f.flush()
